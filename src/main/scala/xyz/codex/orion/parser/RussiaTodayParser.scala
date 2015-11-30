package xyz.codex.orion.parser

import xyz.codex.orion.ArticleData

import scala.xml._
import scalaj.http._

/**
  *
  * @author Nostr
  */
case object RussiaTodayParser extends Parser{

  override def parse(task: String): Option[ArticleData] = {
    println("RT parser is now working!")
    val response: HttpResponse[String] = Http("https://www.rt.com/rss/").asString

    if (response.code == 200){
      val xml = XML.loadString(response.body)

      val items = xml \\ "item"
      for (item <- items) {
        println(item \\ "title")
      }
    }

    // FIXME return the meaning
    None
  }
}
