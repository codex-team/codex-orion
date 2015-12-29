package xyz.codex.orion

/**
  * Definition of DSL objects.
  *
  * @author eliseev
  */
package object twitter {
  case class Tweet(id: String, retweetsCount: Int, favouritesCount: Int, text: String)
}
