package xyz.codex.orion.parser

import java.net.URL
import org.json4s.native.Json
import org.jsoup.Jsoup
import xyz.codex.orion.ArticleData
import org.slf4j.LoggerFactory
import scala.xml.XML
import scalaj.http.{HttpResponse, Http}
import scala.io.Source.{fromInputStream}
import java.util.regex._

/**
  * Russia Today Parser implementation
  * @author A. Menshikov (Nostr @dsnostr)
  */
class RTParser extends BaseParser {

  val baseUrl : URL =  new URL("https://www.rt.com/rss/")
  val logger  = LoggerFactory.getLogger(this.getClass)

  override def getArticle(link : String): Option[ArticleData] = {

    val response: scalaj.http.HttpResponse[String] = scalaj.http.Http(link.toString()).asString

    if (response.code != 200) {
      logger.warn(s"Error code {$response.code} on {$response.location}")
      return None
    }
    else {
      val html = Jsoup.parse(response.body)
      val title = html.select("h1").first().text()
      val text = html.getElementsByClass("article__text").toString()

      var comments_count : Option[Integer] = this.getCommentsCount(link)

      return Some(new ArticleData(publisher = "RT", title = title, text = text, url = link.toString(), commentsCount = comments_count))
    }
  }

  override def getLinks() : Seq[Option[String]] = {
    val response: HttpResponse[String] = Http(baseUrl.toString()).asString

    if (response.code != 200) {
      logger.warn(s"Error code {$response.code} on {$response.location}")
      return Seq(None)
    }
    else {
      val xml = XML.loadString(response.body)
      val items = xml \\ "item"

      for (item <- items.take(5)) yield Some(item \\ "link" text)
    }
  }

  private def getCommentsCount(link : String) : Option[Integer] = {

    val numPattern = "[0-9]+".r
    val xid = numPattern.findFirstIn(link.toCharArray).getOrElse("0")

    val response = Http("https://c1n1.hypercomments.com/api/comments?_=1450115380944").postForm(
      Seq(
        "data" -> "{\"widget_id\":25736,\"xid\":".concat(xid).concat(",\"limit\":1}"),
        "host" -> "https://www.rt.com"
      )
    ).asString

    if (response.code != 200) {
      logger.warn(s"Error code {$response.code} on {$response.location}")
      return None
    }
    else {

      val commentsPattern = """\\\"cm2\\\":(\d+)""".r
      val commentsCount = commentsPattern.findFirstIn(response.body.toCharArray).getOrElse("--------1")

      return Some(commentsCount.splitAt(8)._2.toInt)
    }
  }

  override def getName() : String = "RTParser"
}
