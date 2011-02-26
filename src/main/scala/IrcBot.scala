package net.evilmonkeylabs.scalgore

import org.jibble.pircbot._
import net.lag.configgy.Configgy
import akka.actor.Actor._
import akka.util._

class IrcBot(val name: String,
             val verbose: Boolean) extends PircBot with Logging {

  log.info("Starting up ScalGore")
  setName(name)
  setVerbose(verbose)

  val ircLog = actorOf[IrcLogger].start

  override def onAction(sender: String, login: String, 
                        hostname: String, target: String,
                        action: String) = {
    log.trace("[ACT] {%s} <%s:%s@%s> %s", target, sender, login, hostname, action)
    ircLog ! IrcAction(sender, login, hostname, action, target)
  }
  
  override def onMessage(channel: String, sender: String, 
                         login: String, hostname: String, 
                         message: String) = {
    log.trace("[MSG] {%s} <%s:%s@%s> %s", channel, sender, login, hostname, message)
    ircLog ! IrcPublicMessage(channel, sender, login, hostname, message)
  }
  
  override def onPrivateMessage(sender: String, login: String, 
                                hostname: String, message: String) = {
    log.trace("[PRIVMSG] <%s:%s@%s> %s", sender, login, hostname, message)
    ircLog ! IrcPrivateMessage(sender, login, hostname, message)
  }

  def join(conn: Connection) = {
    log.info("Connecting to network [%s] at [%s]", conn.name, conn.uri)
    connect(conn.uri)
    conn.channels.foreach { joinChannel(_) }
  }
}

// vim: set ts=2 sw=2 sts=2 et:
