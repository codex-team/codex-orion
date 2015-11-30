package xyz.codex.orion

import akka.actor.Actor
import akka.event.Logging
import com.typesafe.scalalogging.Logger
import xyz.codex.orion.ArticlePostProcessor.PostProcessArticle

/**
  *
  * @author eliseev
  */
object ArticlePostProcessor {
  case class PostProcessArticle(article: ArticleData)
}

class ArticlePostProcessor extends Actor{
  private val logger = new Logger(Logging(context.system, this))

  override def receive: Receive = {
    case PostProcessArticle(article) =>
      logger.info(s"Handling article from url=${article.url} with title=${article.title}")

      logger.debug(s"Handling article from url=${article.url} with title=${article.title} " +
        s"with text=${article.text}, comments=${article.comments}.")
  }
}
