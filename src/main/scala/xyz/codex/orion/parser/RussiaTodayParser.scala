package xyz.codex.orion.parser

import scalaj.http._
import scala.xml._
import java.net._
import scala.io.Source

/**
  *
  * @author Nostr
  */
class RussiaTodayParser {

  /**
    * Main method
    */
  def run() {
    println("RT parser is now working!")
    val response: HttpResponse[String] = Http("https://www.rt.com/rss/").asString

    if (response.code == 200){
      val xml = XML.loadString(response.body)

      val items = xml \\ "item"
      for (item <- items) {
        println(item \\ "title")
      }
    }

  }

}
