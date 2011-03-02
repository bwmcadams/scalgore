/**
 * ScalGore simple #akka IRC Bot.
 *
 * Brendan W. McAdams <bwmcadams@evilmonkeylabs.com>
 */
package net.evilmonkeylabs.scalgore

import akka.actor._
import akka.util._
import com.mongodb.casbah.Imports._
import org.elasticsearch.client.transport._
import org.elasticsearch.common.transport._
import org.elasticsearch.client.Requests._
import org.elasticsearch.common.xcontent.XContentBuilder._

trait IrcMessage {
  val sender: String
  val login: String
  val hostname: String
  val message: String
  val date = new java.util.Date // capture date as soon as object is created
}

case class IrcAction(val network: String,
                     val sender: String,
                     val login: String,
                     val hostname: String,
                     val message: String,
                     val target: String) extends IrcMessage

case class IrcPublicMessage(val network: String,
                            val channel: String,
                            val sender: String,
                            val login: String,
                            val hostname: String,
                            val message: String) extends IrcMessage

case class IrcPrivateMessage(val network: String,
                             val sender: String,
                             val login: String,
                             val hostname: String,
                             val message: String) extends IrcMessage


class IrcLogger extends Actor with Logging {
  val mongo = MongoConnection()("ircLogs")
  // val es = new TransportClient().addTransportAddress(new InetSocketTransportAddress("localhost", 9300))

  def parseMessage(msg: String) = msg.split(" ")
  def fixChanName(network: String, name: String) = network ++ name.replaceAll("#", "HASH")
  
  def receive = {
    case pub @ IrcPublicMessage(network, channel, sender, login, hostname, message) => {
      val name = fixChanName(network, channel)
      val kws = parseMessage(message)
      val idx = MongoDBObject("sender" -> 1, "keywords" -> 1)
      mongo(name).ensureIndex(idx)
      val obj = MongoDBObject("type" -> "publicMessage", "channel" -> channel, "sender" -> sender, "login" -> login,
                 "hostname" -> hostname, "message" -> message, "keywords" -> kws, "date" -> pub.date)
      // index in elasticsearch
      log.info(obj.toString)
      // val response = es.index(indexRequest("irclogs").`type`("message")
      //                      .source(obj.toString)).actionGet()
      mongo(name) += (obj)
    }
    case priv @ IrcPrivateMessage(network, sender, login, hostname, message) => {
      val obj = MongoDBObject("type" -> "privateMessage", "sender" -> sender, "login" -> login,
                 "hostname" -> hostname, "message" -> message)
      //mongo("privateMessages") insert(obj) 
    }
    case act @ IrcAction(network, sender, login, hostname, message, target) => {
      val name = fixChanName(network, target)
      val kws = parseMessage(message)
      val obj = MongoDBObject("type" -> "action", "target" -> target, "sender" -> sender, "login" -> login,
                 "hostname" -> hostname, "message" -> message, "keywords" -> kws, "date" -> act.date)
      val idx = MongoDBObject("sender" -> 1, "keywords" -> 1)
      mongo(name).ensureIndex(idx)
      // index in elasticsearch
      log.info(obj.toString)
      // val response = es.index(indexRequest("irclogs").`type`("action")
      //                      .source(obj.toString)).actionGet()
      mongo(name) += (obj)
    }
    case unknown => {}
  }
}

// vim: set ts=2 sw=2 sts=2 et:
