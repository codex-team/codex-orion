package xyz.codex.orion.parser

import java.net.URL

import xyz.codex.orion.ArticleData

/**
  *
  * @author eliseev
  */
// TODO write anything!!! 
case object DummyParser extends Parser {
  override def parse(task: String) =
    Some(ArticleData("Any", new URL(s"http://dummy.com/$task"), "title", "text"))
}
