package xyz.codex.orion

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.stream.ActorMaterializer
import xyz.codex.orion.ParserDispatcher.DispatcherTask
import xyz.codex.orion.parser.RussiaTodayParser
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

  implicit val materializer = ActorMaterializer()

  // Article Context (move to separate actor
  val postProcessor: ActorRef = system.actorOf(Props[ArticlePostProcessor], "postProcessor")
  val parserDispatcher = system.actorOf(Props[ParserDispatcher], "dispatcher")

  // Twitter Context
  val analytic = system.actorOf(Props(new TwitterAnalytic), "twitter-analysis")
  val twitterStream = system.actorOf(Props(
    new TwitterStreamActor(TwitterStreamActor.twitterUri, analytic)
      with OAuthTwitterAuthorization), "twitter-parser")

  // FIXME uncomment to get the consoleful of twitter logs =)
  twitterStream ! "ted"

  // TODO во время тика можно делать намного более хитрую логику, в том числе определять пул задач и приоритеты для выполнения парсинга.
  // Парсинг должен проходить в несколько шагов, в первую очередь определение приоритетов и списка статей, далее отсылка заданий парсерам и третье, сбор всей информации в одном месте
  system.scheduler.schedule(0 seconds, 5 seconds, parserDispatcher, DispatcherTask("Test", RussiaTodayParser))
}