package xyz.codex.orion.parser

import xyz.codex.orion.ArticleData

/**
  *
  * @author eliseev
  */
// TODO write anything!!! 
trait Parser {
  def parse(task: String): Option[ArticleData]
}
