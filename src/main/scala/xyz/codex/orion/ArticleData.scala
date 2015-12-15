package xyz.codex.orion

import java.net.URL
import java.util.{Calendar, Date}


/**
  * Unhandled article data, parsed from
  *
  * @param publisher            publisher
  * @param url                  url resource
  * @param title                title of the article
  * @param text                 text of the article
  * @param comments             list of article's comment if presented
  * @param commentsCount        comments count if presented
  * @param socialAccountStat    map from social media name to number of reposts (likes) if presented
  */
case class ArticleData(publisher: String,
                  url: String,
                  title: String,
                  text: String,
                  comments: Option[List[String]] = Option.empty,
                  commentsCount: Option[Integer] = Option.empty,
                  socialAccountStat: Option[Map[String, Integer]] = Option.empty,
                  updateTime: Date = Calendar.getInstance().getTime()) {

}
