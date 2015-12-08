package xyz.codex.orion.parser

import java.net.URL
import org.jsoup.Jsoup
import xyz.codex.orion.ArticleData

import scala.xml.XML


/**
  * Test Parser implementation, which performs simple HTTP GET
  * @author A. Menshikov (Nostr @dsnostr)
  */
class RTParser extends BaseParser {

  val baseUrl : URL =  new URL("https://www.rt.com/rss/")

  override def getLinks(): Option[List[URL]] = {

    var result : List[URL] = List()
    val response: scalaj.http.HttpResponse[String] = scalaj.http.Http(baseUrl.toString()).asString

    if (response.code != 200) {
      //logger.warning("FAILED to GET: %s WITH CODE %s".format(response.location, response.code))
      println(response.code)
      return None
    }
    else {
      val xml = XML.loadString(response.body)
      val items = xml \\ "item"

      for (item <- items.take(5)) {
        result :::= List(new URL(item \\ "link" text))
      }
      println(result)
      return Some(result)
    }
  }

  override def parseLink(link : URL): Option[ArticleData] = {

    val response: scalaj.http.HttpResponse[String] = scalaj.http.Http(link.toString()).asString

    if (response.code != 200) {
      println(response.code, response.location)
      return None
    }
    else {
      val html = Jsoup.parse(response.body)
      val title = html.select("h1").first().text()
      val text = html.getElementsByClass("article__text").toString()
      return Some(new ArticleData(publisher = "RT", title = title, text = text, url = link))
    }
  }

  override def getName() : String = { return "RTParser" }
}
