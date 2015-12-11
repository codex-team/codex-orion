package xyz.codex.orion.parser

import xyz.codex.orion.ArticleData



/**
  * Интерфейс базового парсера
  * @author A. Menshikov (Nostr @dsnostr)
  */
trait BaseParser {

  /**
    * Parse RSS or other sources and return links to articles from one by one
    */
  def getLinks(): Seq[Option[String]]


  /**
    * Parse article data and return it
    * @param link - string URL
    */
  def getArticle(link : String): Option[ArticleData]


  /**
    * Returns self name
    * @return name : String
    */
  def getName() : String
}