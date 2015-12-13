package xyz.codex.orion.twitter

import spray.http.HttpEntity
import spray.httpx.unmarshalling.{Deserialized, MalformedContent, Unmarshaller}
import spray.json._

import scala.util.Try

/**
  *
  * @author eliseev
  */
// TODO write anything!!! 
trait TweetMarshaller {

  implicit object TweetUnmarshaller extends Unmarshaller[Tweet] {

    def apply(entity: HttpEntity): Deserialized[Tweet] = {
      Try {
        val json = JsonParser(entity.asString).asJsObject
        (json.fields.get("id_str"), json.fields.get("text"), json.fields.get("favorite_count"),
              json.fields.get("retweet_count")) match {
          case (Some(JsString(id)), Some(JsString(text)), Some(JsNumber(favoriteCount)), Some(JsNumber(retweetCount))) =>
            Right(Tweet(id, retweetCount.toInt, favoriteCount.toInt, text))
          case _ => Left(MalformedContent("bad tweet"))
        }
      }
    }.getOrElse(Left(MalformedContent("bad json")))

  }

}