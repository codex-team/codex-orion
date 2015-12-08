package xyz.codex.orion

import akka.actor.{Props, Actor}
import xyz.codex.orion.parser.BaseParser
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by nostr on 08.12.15.
  */
class ParserActor extends Actor with akka.actor.ActorLogging {

  private val articlesGetter = context.actorOf(Props[ArticlesGetter], "articlesGetter")

  override def receive = {
    case StartCrawling(parser : BaseParser) =>
      log.info("ParserActor got StartCrawling command with \"%s\" parser".format(parser.getName()))
      parser.getLinksAsync().collect({
        case Some(result) =>
          articlesGetter ! GetLinksResult(parser, result)
        case None =>
          log.warning("Failed to get result")
      })
  }
}