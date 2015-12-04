package xyz.codex.orion.twitter

/**
  *
  * @author eliseev
  */
// TODO write anything!!! 
object TwitterDSL {
  case class User(id: String, lang: String, followersCount: Int)

  case class Place(country: String, name: String) {
    override lazy val toString = s"$name, $country"
  }

  case class Tweet(id: String, user: User, text: String, place: Option[Place])
}


