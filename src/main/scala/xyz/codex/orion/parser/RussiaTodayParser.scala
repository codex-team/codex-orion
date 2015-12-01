package xyz.codex.orion.parser

import akka.actor.{ActorSelection, Props, Actor, ActorSystem}
import java.net.{URLEncoder, URL}
import xyz.codex.orion.ArticleData
import xyz.codex.orion.ArticlePostProcessor.PostProcessArticle
import scala.xml.XML

case class RTArticle(link : URL) {}

case class RTArticleProcessor() extends Actor {

  private val postProcessor: ActorSelection = context.actorSelection("../postProcessor")

  def parse(link : URL): Unit = {
    val encoded_url : String = link.getHost + URLEncoder.encode(link.getQuery().toString(), "utf-8")
    println(encoded_url)
    val response: scalaj.http.HttpResponse[String] = scalaj.http.Http(encoded_url).asString
    if (response.code != 200) {
      println("ERROR: ", response.location)
      return None
    }

    postProcessor ! PostProcessArticle(new ArticleData(publisher = "", title = "", text = "", url = new URL("")))
  }

  override def receive = {
    case RTArticle(link) =>
      parse(link)
      println("==========================")
    case unknown =>
      println("==========================")
  }
}


/**
  *
  * @author Nostr
  */
case object RussiaTodayParser extends Parser{

  val basicUrl : String = "https://www.rt.com/rss/"

  override def parse(task: String): Option[ArticleData] = {
    println("RT parser is now working!")

    implicit val system = ActorSystem("rt-parser-actor")
    //val log = Logging(system, getClass)

    //val pipeline: HttpRequest => Future[HttpResponse] = sendReceive
    //val request = Get(basicUrl)
    //val response: Future[HttpResponse] = pipeline(request)

    val articleProcessor = system.actorOf(Props[RTArticleProcessor], "article-processor")

    val response: scalaj.http.HttpResponse[String] = scalaj.http.Http(basicUrl).asString

    // TODO: Add retry attempts if needed
    if (response.code != 200) {
      return None
    }

    val xml = XML.loadString(response.body)
    val items = xml \\ "item"

    for (item <- items.take(5)) {
      println(item \\ "title" text, item \\ "description" text)
      articleProcessor ! RTArticle(new URL(item \\ "link" text))
      //act ! RTArticle(new URL(""))
    }


    /*
    response onComplete {
      case Success(result: HttpResponse) =>
        parseXml(result)
      case Success(somethingUnexpected) =>
        log.warning("Success with unexpected data")
      case Failure(error) =>
        log.warning("Failure: ", error)
      case _ =>
        log.error("ERROR")
    }
    */
    val returnObject = ArticleData(publisher = "", title = "", text = "", url = new URL(basicUrl))
    return Option(returnObject)
  }
}
