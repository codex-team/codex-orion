package xyz.codex.orion

import akka.actor.{ActorRef, ActorSystem, Props}
import xyz.codex.orion.parser.ArticlesParser
import scala.concurrent.duration._
import akka.stream.ActorMaterializer
import xyz.codex.orion.twitter.TwitterStreamActor.TrackWords
import xyz.codex.orion.twitter.{OAuthTwitterAuthorization, TwitterAnalytic, TwitterStreamActor}

import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

/**
  *
  * @author eliseev
  */
object Main extends App {
  implicit val system = ActorSystem("Orion-Parser")
  implicit val executorContext = system.dispatcher

  import system.dispatcher
  implicit val materializer = ActorMaterializer()

  // Twitter Context
  val analytic = system.actorOf(Props(new TwitterAnalytic), "twitter-analysis")
  val twitterStream = system.actorOf(Props(
    new TwitterStreamActor(TwitterStreamActor.twitterUri, analytic)
      with OAuthTwitterAuthorization), "twitter-parser")

  // FIXME try to change words and see console full of logs.
  // twitterStream ! TrackWords(List("Russia"))

  // Article Context
  val articlesCrawlerDispatcher: ActorRef = system.actorOf(Props[ArticlesCrawlerDispatcher], "articlesCrawlerDispatcher")
  val articlesParser: ActorRef = system.actorOf(Props[ArticlesParser], "articlesParser")
  val articlesLoader: ActorRef = system.actorOf(Props[ArticlesLoaderActor], "articlesLoader")

  articlesCrawlerDispatcher ! LaunchCrawlersToGetLinks
  //TODO: EXAMPLE: system.scheduler.schedule(0 seconds, 15 seconds, articlesCrawlerDispatcher, LaunchCrawlersToGetLinks)
}