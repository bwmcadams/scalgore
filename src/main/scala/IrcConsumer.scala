package net.evilmonkeylabs.scalgore

import net.lag.configgy.Configgy
import akka.actor.{Actor, ActorRef}
import akka.util._
import akka.camel._

class IrcConsumer(network: Network, ircLog: ActorRef) extends Actor with Consumer with Logging {

  lazy val _endpointUri = "irc:%s@%s?channels=%s" format (network.nick, network.host, network.channels.mkString(","))

  def endpointUri = _endpointUri

  def receive = {
    case Message(body: String, headers: Map[String, String]) => {
      for ( sender <- headers.get("irc.user.nick");
              host <- headers.get("irc.user.host");
             login <- headers.get("irc.user.username");
           channel <- headers.get("irc.target");
           msgtype <- headers.get("irc.messageType") if msgtype == "PRIVMSG") {
        ircLog ! IrcPublicMessage(network.name, channel, sender, login, host, body)
      } // for
    } // case
  } // receive
}

// vim: set ts=2 sw=2 sts=2 et:
