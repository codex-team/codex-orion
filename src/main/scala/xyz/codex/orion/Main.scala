package xyz.codex.orion

import akka.actor.{ActorRef, ActorSystem, Props}

/**
  *
  * @author eliseev
  */
object Main extends App{
  val system = ActorSystem("MySystem")

  val sitesCrawler: ActorRef = system.actorOf(Props[SitesCrawler], "sitesCrawler")

  sitesCrawler ! LaunchCrawlersToGetLinks
}