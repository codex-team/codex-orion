package xyz.codex.orion

import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory

/**
  *
  * @author eliseev
  */
// TODO write anything!!! 
trait Logging {
  val log  = Logger(LoggerFactory.getLogger(getClass))
}
