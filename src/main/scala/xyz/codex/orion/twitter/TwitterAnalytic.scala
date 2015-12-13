package xyz.codex.orion.twitter

import akka.actor.{ActorLogging, Actor}

/**
  *
  * @author eliseev
  */
// TODO write anything!!! 
class TwitterAnalytic extends Actor with ActorLogging{
  override def receive: Receive = {
    case tweet: Tweet =>
      if (tweet.retweetsCount > 10)
        log.warning(s"Accepted a mega-urgent tweet: $tweet")
      else
        log.info(s"Just another tweet $tweet")
  }
}
