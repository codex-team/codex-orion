package xyz.codex.orion

import akka.actor.{ActorRef, ActorSystem, Props}
import xyz.codex.orion.parser.ArticlesParser

import scala.concurrent.duration._
/**
  *
  * @author eliseev
  */
object Main extends App{
  val system = ActorSystem("MySystem")

  import system.dispatcher

  val articlesCrawlerDispatcher: ActorRef = system.actorOf(Props[ArticlesCrawlerDispatcher], "articlesCrawlerDispatcher")
  val articlesParser: ActorRef = system.actorOf(Props[ArticlesParser], "articlesParser")
  val articlesLoader: ActorRef = system.actorOf(Props[ArticlesLoaderActor], "articlesLoader")

  //articlesCrawlerDispatcher ! LaunchCrawlersToGetLinks
  system.scheduler.schedule(0 seconds, 15 seconds, articlesCrawlerDispatcher, LaunchCrawlersToGetLinks)
}