package xyz.codex.orion.parser

import java.net.URL

import xyz.codex.orion.{ArticleData, GetLinksResult}

/**
  * Test Parser implementation, which performs simple HTTP GET
  * @author A. Menshikov (Nostr @dsnostr)
  */
class PureParser extends BaseParser {

  override def getLinks(): Option[List[URL]] = {
    Some(List(new URL("http://www.rt.com/"),new URL("http://www.rt.com/")))
  }

  override def parseLink(link : URL): Option[ArticleData] = {
    Some(new ArticleData(publisher = "RT", url = link, title = "title", text = "text"))
  }

  override def getName() : String = { return "PureParser" }
}
