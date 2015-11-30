package xyz.codex.orion

import akka.actor.{ActorRef, ActorSystem, Props}
import xyz.codex.orion.ParserDispatcher.DispatcherTask
import xyz.codex.orion.parser.RussiaTodayParser

import scala.concurrent.duration._

/**
  *
  * @author eliseev
  */
object Main extends App{
  val system = ActorSystem("MySystem")

  val postProcessor: ActorRef = system.actorOf(Props[ArticlePostProcessor], "postProcessor")
  val parserDispatcher = system.actorOf(Props[ParserDispatcher], "dispatcher")

  // Import implicit context ecexutor to process our tasks.
  import system.dispatcher

  // TODO во время тика можно делать намного более хитрую логику, в том числе определять пул задач и приоритеты для выполнения парсинга.
  // Парсинг должен проходить в несколько шагов, в первую очередь определение приоритетов и списка статей, далее отсылка заданий парсерам и третье, сбор всей информации в одном месте
  system.scheduler.schedule(0 seconds, 5 seconds, parserDispatcher, DispatcherTask("RT Test", RussiaTodayParser))
}