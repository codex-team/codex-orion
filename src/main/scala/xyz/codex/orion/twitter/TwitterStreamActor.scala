package xyz.codex.orion.twitter

import akka.actor.{Actor, ActorLogging, ActorRef}
import akka.io.IO
import spray.can.Http
import spray.client.pipelining._
import spray.http._
import xyz.codex.orion.twitter.TwitterStreamActor.{TrackWords, FollowUsers}

import scala.language.postfixOps

/**
  * Actor, that works with twitter streams.
  * It starts connections to twitter streams api, unmarshalls chunked response
  * and sends it to analytic actor.
  *
  * @author eliseev
  */
object TwitterStreamActor {
  val twitterUri = Uri("https://stream.twitter.com/1.1/statuses/filter.json")

  case class FollowUsers(ids: List[Int])

  case class TrackWords(trackedWords: List[String])
}

class TwitterStreamActor(uri: Uri, twitterStreamAnalytic: ActorRef) extends Actor with TweetMarshaller
  with ActorLogging {

  this: TwitterAuthorization =>


  val io = IO(Http)(context.system)

  override def receive: Receive = {
    case FollowUsers(userIds) =>
      log.info(s"Starting the twitter stream for user ids: $userIds")

      val query: String = s"follow=${userIds.mkString(",")}"

      sendRequest(query)

    case TrackWords(trackedWords) =>
      log.info(s"Starting the twitter stream for words: $trackedWords")

      val query: String = s"track=${trackedWords.mkString(",")}"

      sendRequest(query)

    case ChunkedResponseStart(_) =>
      log.info("Processing of twitter stream started.")

    case MessageChunk(entity, _) =>

      // FIXME here and in Akka Streams we faced the same problem, some json breaks in 2 or more chunks =(
      TweetUnmarshaller(entity).fold(_ => (), twitterStreamAnalytic !)

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
