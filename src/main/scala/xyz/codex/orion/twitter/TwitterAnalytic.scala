package xyz.codex.orion.twitter

import akka.actor.{ActorLogging, Actor}
import xyz.codex.orion.twitter.TwitterDSL.Tweet

/**
  *
  * @author eliseev
  */
// TODO write anything!!! 
class TwitterAnalytic extends Actor with ActorLogging{
  override def receive: Receive = {
    case tweet: Tweet =>
      log.info(s"Accepted a $tweet")
  }
}
