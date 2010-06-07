/**
 * ScalGore simple #akka IRC Bot.
 *
 * Brendan W. McAdams <bwmcadams@evilmonkeylabs.com>
 */
package net.evilmonkeylabs.scalgore

import org.jibble.pircbot._
import se.scalablesolutions.akka.actor._
import se.scalablesolutions.akka.util._
import se.scalablesolutions.akka.persistence.mongo._
import com.novus.casbah.mongodb.Implicits._
import com.novus.casbah.mongodb.ScalaMongoConn
import org.elasticsearch.client.transport._
import org.elasticsearch.util.transport._
import org.elasticsearch.client.Requests._
import org.elasticsearch.util.xcontent.builder.XContentBuilder._

import com.mongodb._

trait IrcMessage {
  val sender: String
  val login: String
  val hostname: String
  val message: String
}

case class IrcAction(val sender: String,
                     val login: String,
                     val hostname: String,
                     val message: String,
                     val target: String)

case class IrcPublicMessage(val channel: String,
                            val sender: String,
                            val login: String,
                            val hostname: String,
                            val message: String)

case class IrcPrivateMessage(val sender: String,
                             val login: String,
                             val hostname: String,
                             val message: String)


class IrcLogger extends Actor with Logging {
  val mongo = ScalaMongoConn()("ircLogs")
  val es = new TransportClient().addTransportAddress(new InetSocketTransportAddress("localhost", 9300))
  def parseMessage(msg: String) = msg.split(" ")
  def fixChanName(name: String) = name.replaceAll("#", "HASH")
  def receive = {
    case pub @ IrcPublicMessage(channel, sender, login, hostname, message) => {
      val name = fixChanName(channel)
      val kws = parseMessage(message)
      val idx = ("sender" -> 1, "keywords" -> 1)
      mongo(name).ensureIndex(idx)
      val obj: DBObject = ("type" -> "publicMessage", "channel" -> channel, "sender" -> sender, "login" -> login,
                 "hostname" -> hostname, "message" -> message, "keywords" -> kws)
      // index in elasticsearch
      log.info(obj.toString)
      val response = es.index(indexRequest("irclogs").`type`("message")
                           .source(obj.toString)).actionGet()
      mongo(name) insert(obj)
    }
    case priv @ IrcPrivateMessage(sender, login, hostname, message) => {
      val obj = ("type" -> "privateMessage", "sender" -> sender, "login" -> login,
                 "hostname" -> hostname, "message" -> message)
      //mongo("privateMessages") insert(obj) 
    }
    case act @ IrcAction(sender, login, hostname, message, target) => {
      val name = fixChanName(target)
      val kws = parseMessage(message)
      val obj: DBObject = ("type" -> "action", "target" -> target, "sender" -> sender, "login" -> login,
                 "hostname" -> hostname, "message" -> message, "keywords" -> kws)
      val idx = ("sender" -> 1, "keywords" -> 1)
      mongo(name).ensureIndex(idx)
      // index in elasticsearch
      log.info(obj.toString)
      val response = es.index(indexRequest("irclogs").`type`("action")
                           .source(obj.toString)).actionGet()
      mongo(name) insert(obj)
    }
    case unknown => {}
  }
}

// vim: set ts=2 sw=2 sts=2 et:
