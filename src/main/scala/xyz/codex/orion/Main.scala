package xyz.codex.orion

import akka.actor.{Cancellable, ActorSystem, Props}
import xyz.codex.orion.ParserDispatcher.Twitter
import xyz.codex.orion.ParserDispatcher.RussiaToday

import scala.concurrent.duration._

/**
  *
  * @author eliseev
  */
object Main {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("MySystem")

    import system.dispatcher

    val parserDispatcher = system.actorOf(Props[ParserDispatcher], "dispatcher")

    // TODO во время тика можно делать намного более хитрую логику, в том числе определять пул задач и приоритеты для выполнения парсинга.
    // Парсинг должен проходить в несколько шагов, в первую очередь определение приоритетов и списка статей, далее отсылка заданий парсерам и третье, сбор всей информации в одном месте
    val schedule: Cancellable = system.scheduler.schedule(0 seconds, 5 seconds, parserDispatcher, Twitter)
    val RussiaTodaySchedule: Cancellable = system.scheduler.schedule(0 seconds, 5 seconds, parserDispatcher, RussiaToday)
  }
}