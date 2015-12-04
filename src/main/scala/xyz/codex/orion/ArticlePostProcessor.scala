package xyz.codex.orion

import akka.actor.{Actor, ActorLogging}
import xyz.codex.orion.ArticlePostProcessor.PostProcessArticle

/**
  *
  * @author eliseev
  */
object ArticlePostProcessor {
  case class PostProcessArticle(article: ArticleData)
}

class ArticlePostProcessor extends Actor with ActorLogging {

  override def receive: Receive = {
    case PostProcessArticle(article) =>
      log.info(s"Handling article from url=${article.url} with title=${article.title}")

      log.debug(s"Handling article from url=${article.url} with title=${article.title} " +
        s"with text=${article.text}, comments=${article.comments}.")
  }
}
