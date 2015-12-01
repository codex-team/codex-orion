package xyz.codex.orion.parser

import xyz.codex.orion.ArticleData

import scala.concurrent.Future

import scala.concurrent.ExecutionContext.Implicits.global

/**
  *
  * @author eliseev
  */
// TODO write anything!!! 
trait Parser {

  /**
    * Realization of parsing, synchronous method. Not visible in public api use [[Parser.parseAsync]] instead.
    */
  protected def parse(task: String): Option[ArticleData]


  /**
    * Public method to parse articles.
    */
  def parseAsync(task: String)() = Future {
    parse(task)
  }

}
