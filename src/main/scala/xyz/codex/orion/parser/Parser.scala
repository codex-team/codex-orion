package xyz.codex.orion.parser

import java.net.URL

import xyz.codex.orion.{ArticleData, GetLinksResult}
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global


/**
  * Интерфейс базового парсера
  * @author A. Menshikov (Nostr @dsnostr)
  */
trait BaseParser {

  /**
    * Website parsing implementation, synchronous method.
    * It's not visible on public api, use [[BaseParser.getLinksAsync]] instead.
    */
  protected def getLinks(): Option[List[URL]]


  /**
    * Article link parsing implementation, synchronous method.
    * It's not visible on public api, use [[BaseParser.parseLinkAsync]] instead.
    */
  protected def parseLink(link : URL): Option[ArticleData]

  /**
    * Get ArticleDate from a link
    * @return Option[ParsingResult]
    */
  def getLinksAsync()() = Future {
    getLinks()
  }

  /**
    * Perform Website crawling
    * @return Option[ParsingResult]
    */
  def parseLinkAsync(link : URL)() = Future {
    parseLink(link)
  }


  /**
    * Returns self name
    * @return name : String
    */
  def getName() : String
}