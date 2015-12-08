package xyz.codex.orion

import java.net.URL

import akka.actor.{Actor, Props}
import xyz.codex.orion.parser.{GetLinksResult, ArticlesParser, BaseParser}
import scala.concurrent.ExecutionContext.Implicits.global


case class GetArticleData(articleData : ArticleData)


/**
  * Created by nostr on 08.12.15.
  */
class ArticlesGetter extends Actor with akka.actor.ActorLogging {

  private val articlesParser = context.actorOf(Props[ArticlesParser], "articlesParser")

  override def receive = {
    case GetLinksResult(parser : BaseParser, result : List[URL]) =>
      log.info("ArticlesGetter got links from %s: \"%s\"".format(parser.getName(), result.toString()))

      // TODO: Maybe we should add some async
      result foreach {

        // TODO: Or there're can be too many threads
        parser.parseLinkAsync(_).collect({
          case Some(result) =>
            articlesParser ! GetArticleData(result)
          case None =>
            log.warning("Failed to parse result")
        })
      }
  }
}
