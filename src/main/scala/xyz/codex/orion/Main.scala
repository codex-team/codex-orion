package xyz.codex.orion

import akka.actor.{ActorRef, ActorSystem, Props}
import xyz.codex.orion.parser.ArticlesParser

/**
  *
  * @author eliseev
  */
object Main extends App{
  val system = ActorSystem("MySystem")

  val articlesCrawlerDispatcher: ActorRef = system.actorOf(Props[ArticlesCrawlerDispatcher], "articlesCrawlerDispatcher")
  val articlesParser: ActorRef = system.actorOf(Props[ArticlesParser], "articlesParser")
  val articlesLoader: ActorRef = system.actorOf(Props[ArticlesLoaderActor], "articlesLoader")

  articlesCrawlerDispatcher ! LaunchCrawlersToGetLinks
}