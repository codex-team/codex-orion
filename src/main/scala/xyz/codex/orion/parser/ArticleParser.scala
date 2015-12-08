package xyz.codex.orion.parser

import akka.actor.Actor
import xyz.codex.orion.{ArticleData, GetArticleData}
import akka.persistence._
import com.mongodb.casbah.Imports._
import com.novus.salat._
import com.novus.salat.global._

/**
  * Created by nostr on 08.12.15.
  */
class ArticlesParser extends Actor with akka.actor.ActorLogging {

  private val uri = MongoClientURI("mongodb://localhost:27017/store.snapshots")
  private val client =  MongoClient(uri)
  private val db = client(uri.database.get)
  private val coll = db(uri.collection.get)

  override def receive = {
    case GetArticleData(articleData : ArticleData) =>
      log.info(s"Parsed article '${articleData.title}' (${articleData.url})")
      val dbo = grater[ArticleData].asDBObject(articleData)
      coll.insert(dbo)
  }
}