package xyz.codex.orion.twitter

import akka.actor.{Actor, ActorLogging, ActorRef}
import akka.io.IO
import spray.can.Http
import spray.client.pipelining._
import spray.http._

import scala.language.postfixOps

/**
  *
  * @author eliseev
  */
object TwitterStreamActor {
  val twitterUri = Uri("https://stream.twitter.com/1.1/statuses/filter.json")
}

class TwitterStreamActor(uri: Uri, processor: ActorRef) extends Actor with TweetMarshaller
  with ActorLogging {

  this: TwitterAuthorization =>


  val io = IO(Http)(context.system)

  override def receive: Receive = {
    case users: List[Int] =>
      log.info(s"Starting the twitter stream for users: $users")

      val query: String = s"follow=${users.mkString(",")}"

      sendRequest(query)
    case track: String =>
      log.info(s"Starting the twitter stream for users: $track")

      val query: String = s"track=$track"

      sendRequest(query)
    case ChunkedResponseStart(_) =>
      log.info("Start to process twitter stream")
    case MessageChunk(entity, _) =>

      // FIXME here and in Akka Streams we faced the same problem, some json breaks in 2 or more chunks =(
      TweetUnmarshaller(entity).fold(_ => (), processor !)
    case somethingElse =>
      log.warning(s"Something wrong happened $somethingElse")
  }

  def sendRequest(entityString: String): Unit = {
    val body = HttpEntity(ContentType(MediaTypes.`application/x-www-form-urlencoded`), entityString)
    val rq = HttpRequest(HttpMethods.POST, uri = uri, entity = body) ~> authorize

    sendTo(io).withResponsesReceivedBy(self)(rq)
  }
}

trait TwitterAuthorization {
  def authorize: HttpRequest => HttpRequest
}

trait OAuthTwitterAuthorization extends TwitterAuthorization {
  import xyz.codex.orion.common.Oauth._

  val consumer = Consumer("LHpEoPpHCOysM13hdalV8Y93Q",
    "E9sWTx1ha3HRQkHMbGsb7lg5ihxHHOcDAziRxLSt0TG2r4gZCe")
  val token = Token("1573711442-lbu7LmCeEdraamveUg1JaVfLnpqqKZjIRmilfUl",
    "n7ixvTVQaw84dNRu1FAAkcEiXtLffdPXH54VtLWhv69ZD")

  val authorize: (HttpRequest) => HttpRequest = oAuthAuthorizer(consumer, token)
}
