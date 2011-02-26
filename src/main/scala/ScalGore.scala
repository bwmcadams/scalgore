/**
 * ScalGore simple #akka IRC Bot.
 *
 * Brendan W. McAdams <bwmcadams@evilmonkeylabs.com>
 */
package net.evilmonkeylabs.scalgore

import org.jibble.pircbot._
import net.lag.configgy.{Config=>CConfig}
import akka.actor._
import Actor._
import akka.config.Supervision._
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

    val irclog = actorOf[IrcLogger].start

    val consumers =
      for (name <- cfg.getList(Config.networks);
           host <- cfg.getString(Config.host(name));
           nick <- cfg.getString(Config.nick(name)))
        yield actorOf(new IrcConsumer(Network(name, host, nick, cfg.getList(Config.channels(name))), irclog))

    val supervisor = Supervisor(
      SupervisorConfig(
        OneForOneStrategy(List(classOf[Exception]), 3, 10),
        Supervise(irclog, Permanent) +:
        consumers.map { a => Supervise(a, Permanent) }.toList))

    supervisor.start
  }
}

// vim: set ts=2 sw=2 sts=2 et:
