package xyz.codex.orion

import akka.actor.{Actor, ActorSelection}
import akka.event.Logging
import xyz.codex.orion.ArticlePostProcessor.PostProcessArticle
import xyz.codex.orion.ParserDispatcher._
import xyz.codex.orion.parser.Parser

/**
  *
  * @author eliseev
  */

object ParserDispatcher {
  case class DispatcherTask(task: String, parser: Parser)
}

class ParserDispatcher extends Actor {
  private val logger = Logging(context.system, this)

  private val postProcessor: ActorSelection = context.actorSelection("../postProcessor")

  override def receive: Receive = {
    case DispatcherTask(task, parser) =>
      val mayBeArticleData = parser.parse(task)
      logger.debug("Parsed article with data={}", mayBeArticleData)

      mayBeArticleData match {
        case Some(articleData) => postProcessor ! PostProcessArticle(articleData)
        case None => logger.warning(s"Failed to parse $task")
      }

    case unknown => logger.warning("Failed to parse {}", unknown)
  }
}


