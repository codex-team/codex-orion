package xyz.codex.orion.parser

import akka.actor.ActorSystem
import akka.event.Logging
import xyz.codex.orion.ArticleData
import java.net.URL
import scala.concurrent.Future
import spray.http.{HttpResponse, HttpRequest}
import spray.client.pipelining._
import scala.util.{Success, Failure}
import akka.io.IO
import scala.concurrent.duration._

case class RTParsingResult()

/**
  *
  * @author Nostr
  */
case object RussiaTodayParser extends Parser{

  def parseXml(result: HttpResponse): Unit = {
    println(result.status)
  }

  override def parse(task: String): Option[ArticleData] = {
    println("RT parser is now working!")
    //val response: HttpResponse[String] = Http("https://www.rt.com/rss/").asString

    implicit val system = ActorSystem("rt-parser-actor")
    import system.dispatcher
    val log = Logging(system, getClass)

    val pipeline: HttpRequest => Future[HttpResponse] = sendReceive
    val request = Get("http://maps.googleapis.com/maps/api/elevation/json?locations=27.988056,86.925278&sensor=false")
    val response: Future[HttpResponse] = pipeline(request)

    /*if (response.code == 200){
      val xml = XML.loadString(response.body)

      val items = xml \\ "item"
      for (item <- items) {
        println(item \\ "title")
      }
    }*/

    response onComplete {
      case Success(result) =>
        parseXml(result)
      case Failure(error) =>
        log.warning("ERROR: ", error)

    }

    val returnObject = ArticleData(publisher = "", title = "", text = "", url = new URL("https://www.rt.com/rss/"))
    return Option(returnObject)
  }
}
