package xyz.codex.orion.twitter

import akka.actor.{ActorLogging, Actor, ActorRef}
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
    case query: String =>
      log

      val body = HttpEntity(ContentType(MediaTypes.`application/x-www-form-urlencoded`),
        s"track=$query")
      val rq = HttpRequest(HttpMethods.POST, uri = uri, entity = body) ~> authorize

      sendTo(io).withResponsesReceivedBy(self)(rq)
    case ChunkedResponseStart(_) =>

    case MessageChunk(entity, _) =>
      TweetUnmarshaller(entity).fold(_ => (), processor !)

    case _ =>
  }
}

trait TwitterAuthorization {
  def authorize: HttpRequest => HttpRequest
}

trait OAuthTwitterAuthorization extends TwitterAuthorization {
  import xyz.codex.orion.common.OAuth._

  val consumer = Consumer("LHpEoPpHCOysM13hdalV8Y93Q",
    "E9sWTx1ha3HRQkHMbGsb7lg5ihxHHOcDAziRxLSt0TG2r4gZCe")
  val token = Token("1573711442-lbu7LmCeEdraamveUg1JaVfLnpqqKZjIRmilfUl",
    "n7ixvTVQaw84dNRu1FAAkcEiXtLffdPXH54VtLWhv69ZD")

  val authorize: (HttpRequest) => HttpRequest = oAuthAuthorizer(consumer, token)
}
