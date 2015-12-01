package xyz.codex.orion.parser

import akka.actor.{Actor, ActorSelection, ActorSystem}
import akka.event.Logging
import java.net.URL
import xyz.codex.orion.ArticleData
import xyz.codex.orion.ArticlePostProcessor.PostProcessArticle
import scala.concurrent.Future
import spray.http.{HttpResponse, HttpRequest, StatusCode}
import spray.client.pipelining._
import scala.util.{Success, Failure}
import scala.xml.XML

case class RTParsingResult(result: HttpResponse) {}

/*case class RTXMlParser extends Actor {

  private val logger = Logging(context.system, this)
  private val postProcessor: ActorSelection = context.actorSelection("../postProcessor")

  def parseXml(result: HttpResponse): Option[ArticleData] = {
    if (result.status == StatusCode.int2StatusCode(200)) {
      println("OK")

      val xml = XML.loadString(result.entity.data.asString)
      val items = xml \\ "item"

      for (item <- items) {
        println(item \\ "title" text, item \\ "description" text)
      }

      //val (publisher, title, text) = ("", "", "")
      return new ArticleData(publisher = "", title = "", text = "", url = new URL(basicUrl))
    }
    else {
      println("ERR")
      return new ArticleData(publisher = "", title = "", text = "", url = new URL(basicUrl))
    }
  }

  override def receive: Receive = {
    case RTParsingResult(result: HttpResponse) =>

      val mayBeArticleData = parseXml(result)
      mayBeArticleData match {
        case Some(articleData) => postProcessor ! PostProcessArticle(articleData)
        case None => logger.warning(s"Failed to parse RT article")
      }
  }
}
*/

/**
  *
  * @author Nostr
  */
case object RussiaTodayParser extends Parser{

  val basicUrl : String = "https://www.rt.com/rss/"

  def parseXml(result: HttpResponse): (String, Boolean) = {
    if (result.status == StatusCode.int2StatusCode(200)) {
      println("OK")

      val xml = XML.loadString(result.entity.data.asString)
      val items = xml \\ "item"

      for (item <- items.take(2)) {
        println(item \\ "title" text, item \\ "description" text)
      }

      return ("", true)
    }
    else {
      println("ERR")
      return ("", false)
    }
  }

  override def parse(task: String): Option[ArticleData] = {
    println("RT parser is now working!")

    implicit val system = ActorSystem("rt-parser-actor")
    import system.dispatcher
    val log = Logging(system, getClass)

    val pipeline: HttpRequest => Future[HttpResponse] = sendReceive
    val request = Get(basicUrl)
    val response: Future[HttpResponse] = pipeline(request)

    response onComplete {
      case Success(result: HttpResponse) =>
        parseXml(result)
      case Success(somethingUnexpected) =>
        log.warning("Success with unexpected data")
      case Failure(error) =>
        log.warning("Failure: ", error)
      case _ =>
        log.error("ERROR")
    }

    val returnObject = ArticleData(publisher = "", title = "", text = "", url = new URL(basicUrl))
    return Option(returnObject)
  }
}
