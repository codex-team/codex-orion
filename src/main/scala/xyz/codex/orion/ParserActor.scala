package xyz.codex.orion

import akka.actor.{Actor, Props, ActorLogging}
import xyz.codex.orion.parser.{GetArticleData, ArticlesParser, BaseParser}

case class LoadArticleLinks(parser : BaseParser)
case class LoadArticle(parser : BaseParser, link : String)


/**
  * Created by nostr on 08.12.15.
  */
class ParserActor extends Actor with ActorLogging {

  private val articlesParser = context.actorSelection("/user/articlesParser")

  override def receive = {
    case LoadArticleLinks(parser : BaseParser) =>
      log.info("ParserActor got StartCrawling command with \"%s\" parser".format(parser.getName()))

      parser.getLinks() foreach {
        case Some(link) =>
          self ! LoadArticle(parser, link)
        case None =>
          log.warning("Failed to load links")
      }

    case LoadArticle(parser : BaseParser, link : String) =>
      parser.getArticle(link) match {
        case Some(result) =>
          articlesParser ! GetArticleData(result)
        case None =>
          log.warning("Failed to load article")
      }

    case _ =>
      log.warning("Unknown message")
  }
}