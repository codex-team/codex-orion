package xyz.codex.orion

import akka.actor._
import xyz.codex.orion.parser.{PureParser, BaseParser}


case class StartCrawling(parser : BaseParser)
case class LaunchCrawlersToGetLinks()


/**
  * Created by nostr on 03.12.15.
  */
class SitesCrawler extends Actor with akka.actor.ActorLogging {

  private val crawlersList = Map(
    context.actorOf(Props[ParserActor]) -> StartCrawling(new PureParser)
  )

  override def receive = {
    case LaunchCrawlersToGetLinks =>
      log.info("SitesCrawler got LaunchCrawlersToGetLinks command")
      crawlersList foreach {
        case (actor, command) => actor ! command
      }

    case _ =>
      log.warning("Something")
  }

}