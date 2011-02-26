package net.evilmonkeylabs.scalgore

import org.jibble.pircbot._
import net.lag.configgy.Configgy
import akka.actor.Actor._
import akka.util._

class IrcBot(val name: String,
             val verbose: Boolean,
             val server: String,
             val channel: String) extends PircBot with Logging {

  log.info("Setting up Scala IRC bot (%s/%s).", server, channel)
  setName(name)
  setVerbose(verbose)
  connect(server)
  joinChannel(channel)
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
}

// vim: set ts=2 sw=2 sts=2 et:
