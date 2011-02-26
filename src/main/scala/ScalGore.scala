/**
 * ScalGore simple #akka IRC Bot.
 *
 * Brendan W. McAdams <bwmcadams@evilmonkeylabs.com>
 */
package net.evilmonkeylabs.scalgore

import org.jibble.pircbot._
import net.lag.configgy.{Config=>CConfig}
import akka.actor.Actor.actorOf
import akka.util._
import akka.camel._

object Config {
	val networks = "scalgore.networks"

	val host: (String)=>String = "scalgore.%s.host" format _
	val channels: (String)=>String = "scalgore.%s.channels" format _
	val nick: (String)=>String = "scalgore.%s.nick" format _
}

case class Network(name: String, host: String, nick: String, channels: Seq[String])

object ScalGore extends Logging {

  def main(args: Array[String]) {
    val cfg = CConfig.fromResource("scalgore.conf", getClass.getClassLoader)

    CamelServiceManager.startCamelService

    for (n <- cfg.getList(Config.networks);
    	   host <- cfg.getString(Config.host(n));
    	   nick <- cfg.getString(Config.nick(n))) {
      val network = Network(n, host, nick, cfg.getList(Config.channels(n)))
      log.info("Starting actor for: %s", network)
      actorOf(new IrcConsumer(network)).start
   	}
  }
}

// vim: set ts=2 sw=2 sts=2 et:
