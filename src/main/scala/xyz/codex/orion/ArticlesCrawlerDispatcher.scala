package xyz.codex.orion

import akka.actor.{Actor, ActorLogging, Props}
import xyz.codex.orion.parser.RTParser


case class LaunchCrawlersToGetLinks()


/**
  * Created by nostr on 03.12.15.
  */
class ArticlesCrawlerDispatcher extends Actor with ActorLogging {

  private val crawlersList = Map(
    context.actorOf(Props[LinksLoaderActor]) -> LoadArticleLinks(new RTParser)
  )

  override def receive = {
    case LaunchCrawlersToGetLinks =>
      log.info("ArticlesCrawlerDispatcher launched")

      crawlersList foreach {
        case (actor, command) => actor ! command
      }

    case _ =>
      log.warning("Unknown message in articlesCrawlerDispatcher")
  }

}