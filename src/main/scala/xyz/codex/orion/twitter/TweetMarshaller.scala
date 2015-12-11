//package xyz.codex.orion.twitter
//
//import akka.dispatch.Futures
//import akka.http.scaladsl.model.HttpResponse
//import akka.http.scaladsl.unmarshalling.FromResponseUnmarshaller
//import akka.http.scaladsl.util.FastFuture
//import akka.stream.Materializer
//import spray.json.JsonParser
//import xyz.codex.orion.twitter.TwitterDSL.Tweet
//
//import scala.concurrent.{ExecutionContext, Future}
//
///**
//  *
//  * @author eliseev
//  */
//// TODO write anything!!!
//trait TweetMarshaller {
//
//  implicit object TweetUnmarshaller extends FromResponseUnmarshaller[Tweet] {
//
//    //    def mkUser(user: JsObject): Deserialized[User] = {
//    //      (user.fields("id_str"), user.fields("lang"), user.fields("followers_count")) match {
//    //        case (JsString(id), JsString(lang), JsNumber(followers)) => Right(User(id, lang, followers.toInt))
//    //        case (JsString(id), _, _) => Right(User(id, "", 0))
//    //        case _ => Left(MalformedContent("bad user"))
//    //      }
//    //    }
//    //
//    //    def mkPlace(place: JsValue): Deserialized[Option[Place]] = place match {
//    //      case JsObject(fields) =>
//    //        (fields.get("country"), fields.get("name")) match {
//    //          case (Some(JsString(country)), Some(JsString(name))) => Right(Some(Place(country, name)))
//    //          case _ => Left(MalformedContent("bad place"))
//    //        }
//    //      case JsNull => Right(None)
//    //      case _ => Left(MalformedContent("bad tweet"))
//    //    }
//    //
//    //    def apply(entity: HttpEntity): Deserialized[Tweet] = {
//    //      Try {
//    //        val json = JsonParser(entity.asString).asJsObject
//    //        (json.fields.get("id_str"), json.fields.get("text"), json.fields.get("place"), json.fields.get("user")) match {
//    //          case (Some(JsString(id)), Some(JsString(text)), Some(place), Some(user: JsObject)) =>
//    //            val x = mkUser(user).fold(x => Left(x), { user =>
//    //              mkPlace(place).fold(x => Left(x), { place =>
//    //                Right(Tweet(id, user, text, place))
//    //              })
//    //            })
//    //            x
//    //          case _ => Left(MalformedContent("bad tweet"))
//    //        }
//    //      }
//    //    }.getOrElse(Left(MalformedContent("bad json")))
//    override def apply(value: HttpResponse)(implicit ec: ExecutionContext, materializer: Materializer): Future[Tweet] = {
//      try{
//        JsonParser()
//      }
//      catch {
//        case x: Exception => FastFuture.failed(x)
//      }
//    }
//  }
//
//}