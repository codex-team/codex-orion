package xyz.codex.orion.parser

import akka.actor.Actor
import xyz.codex.orion.{ArticleData, GetArticleData}

/**
  * Created by nostr on 08.12.15.
  */
class ArticlesParser extends Actor with akka.actor.ActorLogging {

  override def receive = {
    case GetArticleData(articleData : ArticleData) =>
      log.info(s"Parsed article '${articleData.title}' (${articleData.url})")
  }
}