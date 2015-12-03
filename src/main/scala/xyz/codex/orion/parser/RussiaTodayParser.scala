package xyz.codex.orion.parser

import akka.actor._
import java.net.URL
import akka.event.Logging
import org.jsoup.Jsoup
import xyz.codex.orion.parser.RTArticleProcessor.RTArticle
import xyz.codex.orion.{ArticlePostProcessor, ArticleData}
import xyz.codex.orion.ArticlePostProcessor.PostProcessArticle
import scala.xml.XML

object RTArticleProcessor {
  case class RTArticle(link : URL) {}
}

class RTArticleProcessor extends Actor {

  val postProcessor: ActorRef = context.actorOf(Props[ArticlePostProcessor], "postProcessor")
  private val logger = Logging(context.system, this)

  def parse(link : URL): Unit = {
    val response: scalaj.http.HttpResponse[String] = scalaj.http.Http(link.toString()).asString

    if (response.code != 200) {
      logger.error("FAILED to GET: %s WITH CODE %s".format(response.location, response.code))
      return None
    }

    val html = Jsoup.parse(response.body)
    val title = html.select("h1").first().text()
    val text = html.getElementsByClass("article__text").toString()

    postProcessor ! PostProcessArticle(new ArticleData(publisher = "RT", title = title, text = text, url = link))
  }

  override def receive = {
    case RTArticle(link) =>
      parse(link)
    case unknown =>
      println("ERROR")
  }
  // TODO: Is this actor closed?
}


/**
  *
  * @author Nostr
  */
case object RussiaTodayParser extends Parser{

  val basicUrl : String = "https://www.rt.com/rss/"

  override def parse(task: String): Option[ArticleData] = {
    implicit val system = ActorSystem("MySystem")

    println("RT parser is now working!")

    val articleProcessor = system.actorOf(Props[RTArticleProcessor], "article-processor")

    val response: scalaj.http.HttpResponse[String] = scalaj.http.Http(basicUrl).asString

    // TODO: Add retry attempts if needed
    if (response.code != 200) {
      return None
    }

    val xml = XML.loadString(response.body)
    val items = xml \\ "item"

    // TODO: Remove limit with 5 entities
    for (item <- items.take(5)) {
      articleProcessor ! RTArticle(new URL(item \\ "link" text))
    }

    // Nothing to return. RTArticle'll do everything.
    return None
  }
}
