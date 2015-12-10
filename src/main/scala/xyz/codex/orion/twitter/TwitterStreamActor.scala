package xyz.codex.orion.twitter

import akka.actor.{Actor, ActorLogging, ActorRef}
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.OutgoingConnection
import akka.http.scaladsl.model._
import akka.stream.scaladsl.{Flow, _}

import scala.concurrent.Future
import scala.language.postfixOps

/**
  *
  * @author eliseev
  */
object TwitterStreamActor {
  val twitterUri = Uri("https://stream.twitter.com/1.1/statuses/filter.json")
}

class TwitterStreamActor(uri: Uri, processor: ActorRef) extends Actor with TweetMarshaller
with ActorLogging with ImplicitMaterializer {

  this: TwitterAuthorization =>

  import context._

  private val streamConnection: Flow[HttpRequest, HttpResponse, Future[OutgoingConnection]] =
    Http().outgoingConnectionTls("stream.twitter.com", log = log)


  override def receive: Receive = {
    case query: String =>
      log.info(query)

      val body = HttpEntity(ContentType(MediaTypes.`application/x-www-form-urlencoded`), s"track=$query")
      val authorize1 = authorize(HttpRequest(uri = uri, method = HttpMethods.POST, entity = body))
      val result = Source
        .single(authorize1)
        .via(streamConnection)
        .runForeach(x => log.info(x.toString))
    case x =>
      log.error(x.toString)

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
