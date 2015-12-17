package xyz.codex.orion.twitter

import akka.actor.{ActorLogging, Actor}

/**
  * A good beginning for a great data analysis.
  *
  * @author eliseev
  */
class TwitterAnalytic extends Actor with ActorLogging{
  override def receive: Receive = {
    case tweet: Tweet =>
      if (tweet.retweetsCount > 10)
        log.info(s"Accepted a mega-urgent tweet: $tweet")
      else
        log.debug(s"Just another tweet $tweet")
  }
}
