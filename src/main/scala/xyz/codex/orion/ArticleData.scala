package xyz.codex.orion

import java.net.URL


/**
  * Unhandled article data, parsed from
  *
  * @param publisher            publisher
  * @param url                  url resource
  * @param title                title of the article
  * @param text                 text of the article
  * @param comments             list of article's comment if present.
  * @param socialAccountStat    map from social media name to number of reposts (likes) if present.
  */
case class ArticleData(publisher: String,
                  url: String,
                  title: String,
                  text: String,
                  comments: Option[List[String]] = Option.empty,
                  socialAccountStat: Option[Map[String, Integer]] = Option.empty) {

}
