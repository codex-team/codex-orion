package xyz.codex.orion

import akka.actor.{Props, Actor, ActorLogging}

/**
  * Created by nostr on 03.12.15.
  */
class SitesCrawler extends Actor with akka.actor.ActorLogging {

  private val DBWriterActor = context.actorOf(Props[DBWriterActor])
  private val crawlersList = Map(
    context.actorOf(Props[ParserActor]) -> StartCrawling(new RTParser)
  )

  override def receive = {
    case StartWorking =>
      log.info("SitesCrawler got StartWorking command")
      crawlersList foreach {
        case (actor, command) => actor ! command
      }
    case ParsingResult(result : String) =>
      log.info("SitesCrawler got ParsingResult with \"%s\"".format(result))
      DBWriterActor ! ParsingResult(result)
  }

}

class DBWriterActor extends Actor with akka.actor.ActorLogging {

  override def receive = {
    case ParsingResult(result : String) =>
      log.info("DBWriterActor wrote \"%s\"".format(result))
  }
}

class ParserActor extends Actor with akka.actor.ActorLogging {

  override def receive = {
    case StartCrawling(parser : BaseParser) =>
      log.info("ParserActor got StartCrawling command with \"%s\" parser".format(parser.getName()))
      sender ! parser.parse()
  }
}

case class ParsingResult(result : String)
case class StartCrawling(parser : BaseParser)
case class StartWorking()

trait BaseParser {
  def parse() : ParsingResult
  def getName() : String
}

class RTParser extends BaseParser {

  override def parse(): ParsingResult = {
    return ParsingResult("RT parsing result")
  }

  override def getName() : String = { return "RT Parser" }
}