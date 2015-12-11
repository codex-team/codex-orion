package xyz.codex.orion.parser

import java.net.URL
import org.jsoup.Jsoup
import xyz.codex.orion.ArticleData
import org.slf4j.LoggerFactory
import scala.xml.XML


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
      return Some(new ArticleData(publisher = "RT", title = title, text = text, url = link.toString()))
    }
  }

  override def getLinks() : Seq[Option[String]] = {
    val response: scalaj.http.HttpResponse[String] = scalaj.http.Http(baseUrl.toString()).asString

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

  override def getName() : String = "RTParser"
}
