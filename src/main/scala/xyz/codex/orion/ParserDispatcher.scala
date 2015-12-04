package xyz.codex.orion

import akka.actor.{Actor, ActorLogging, ActorSelection}
import xyz.codex.orion.ArticlePostProcessor.PostProcessArticle
import xyz.codex.orion.ParserDispatcher._
import xyz.codex.orion.parser.Parser

import scala.concurrent.ExecutionContext.Implicits.global

/**
  *
  * @author eliseev
  */

object ParserDispatcher {
  case class DispatcherTask(task: String, parser: Parser)
}

class ParserDispatcher extends Actor with ActorLogging {
  private val postProcessor: ActorSelection = context.actorSelection("../postProcessor")

  override def receive: Receive = {
    case DispatcherTask(task, parser) =>
      log.debug(s"Dispatching task $task to parser $parser")

      parser.parseAsync(task).collect({
          case Some(articleData) =>
            log.debug(s"Parsed article '${articleData.title}' (${articleData.url})")
            postProcessor ! PostProcessArticle(articleData)

          case None =>
            log.warning(s"Failed to parse $task task")
        })

    case unknown => log.warning(s"Failed to parse $unknown")
  }
}


