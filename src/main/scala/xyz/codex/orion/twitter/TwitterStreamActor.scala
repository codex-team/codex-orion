package xyz.codex.orion.twitter

import akka.actor.{Actor, ActorLogging, ActorRef}
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.OutgoingConnection
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.scaladsl.{Flow, _}
import spray.json._
import xyz.codex.orion.Logging
import xyz.codex.orion.twitter.TwitterDSL.Tweet

import scala.concurrent.Future
import scala.language.postfixOps

/**
  *
  * @author eliseev
  */
object TwitterStreamActor {
  val twitterUri = Uri("https://stream.twitter.com/1.1/statuses/filter.json")
}


object TwitterJsonProtocol extends DefaultJsonProtocol {

  implicit object TweetJsonFormat extends RootJsonFormat[Option[Tweet]] with Logging {
    override def read(value: JsValue) = value match {
      case o: JsObject =>
        o.getFields("text", "retweet_count", "id") match {
          case Seq(JsString(text), JsNumber(retweetCount), JsString(id)) =>
            Some(new Tweet(id, text, retweetCount.intValue()))
          case _ =>
            log.warn("Failed to parse tweet")
            None
        }
      case x =>
        log.warn("Non-object obtained")
        None
    }

    override def write(obj: Option[Tweet]): JsValue = JsArray()
  }

}

class TwitterStreamActor(uri: Uri, processor: ActorRef) extends Actor
with ActorLogging with ImplicitMaterializer with SprayJsonSupport {

  this: TwitterAuthorization =>

  import context._

  private val streamConnection: Flow[HttpRequest, HttpResponse, Future[OutgoingConnection]] =
    Http().outgoingConnectionTls("stream.twitter.com", log = log)


  override def receive: Receive = {
    case query: String =>
      log.info(s"Starting work with Twitter Stream Api, tracking '$query'")

      val body = HttpEntity(ContentType(MediaTypes.`application/x-www-form-urlencoded`), s"track=$query")
      val result = Source
        .single(authorize(HttpRequest(uri = uri, method = HttpMethods.POST, entity = body)))
        .via(streamConnection)
        .map(sourceInChunkedResponse)
        .filter(_.isDefined)
        .map(_.get)
        .map(x => Unmarshal(x).to[List[Option[Tweet]]])
        .runForeach(x => x.onComplete(x => log.warning(s"$x")))
    case x =>
      log.error(x.toString)

  }

  def sourceInChunkedResponse: (HttpResponse) => Option[HttpEntity] = {

    case HttpResponse(StatusCodes.OK, _, entity, _) =>
      Some(entity)
    case unexpectedResponse =>
      log.warning(s"Unexpected response: $unexpectedResponse")
      None

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
