/**
 * ScalGore simple #akka IRC Bot.
 *
 * Brendan W. McAdams <bwmcadams@evilmonkeylabs.com>
 */
package net.evilmonkeylabs.scalgore

import org.jibble.pircbot._
import net.lag.configgy.Configgy
import akka.actor._
import akka.util._

object Config {
	val servers = "scalgore.servers"

	val uri: (String)=>String = "scalgore.%s.uri" format _
	val channels: (String)=>String = "scalgore.%s.channels" format _
}

case class Connection(name: String, uri: String, channels: Seq[String])

object ScalGore extends Logging {

  def main(args: Array[String]) {
  	Configgy.configureFromResource("scalgore.conf", getClass.getClassLoader)
    val bot = new IrcBot("ScalGore", true)

    val cfg = Configgy.config



    for (s <- cfg.getList(Config.servers);
    	 uri <- cfg.getString(Config.uri(s))) {
      log.info("lala")
      val conn = Connection(s, uri, cfg.getList(Config.channels(s)))
      bot.join(conn)
   	}
  }
}

// vim: set ts=2 sw=2 sts=2 et:
