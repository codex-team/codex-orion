package xyz.codex.orion

import java.net.URL

import akka.actor._
import xyz.codex.orion.parser.{PureParser, BaseParser}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

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

class ArticlesParser extends Actor with akka.actor.ActorLogging {

  override def receive = {
    case GetArticleData(articleData : ArticleData) =>
      log.info("ArticlesParser got articleData")
  }
}

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

// TODO: Maybe name it LinksResult
case class GetLinksResult(parser : BaseParser, result : List[URL])
// TODO: Maybe name it 'GetLinks'
case class StartCrawling(parser : BaseParser)
case class GetArticleData(articleData : ArticleData)
case class LaunchCrawlersToGetLinks()
