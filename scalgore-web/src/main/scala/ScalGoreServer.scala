package net.evilmonkeylabs.scalgore
package web

import com.mongodb.casbah.Imports._
import com.mongodb.casbah.commons.Logging 
import unfiltered.request._
import unfiltered.response._
import unfiltered.scalate._

class ScalGoreWeb extends unfiltered.filter.Plan with Logging {
  
  val mongo = MongoConnection()("ircLogs")

  def intent = {
    case req @ GET(Path("/")) => 
      log.info("[*] GET /")
      val ctx = List(
        "channels" -> mongo.collectionNames.filter {
          _.startsWith("freenode")
        }.map { x =>
          val c = x.split("HASH")(1)
          c -> "#%s".format(c)
        }
      )
      log.info("context: %s", ctx)
      Ok ~> Scalate(req, "root.jade", ctx: _*)
    case req @ GET(Path(Seg("view" :: channel :: Nil))) => 
      log.info("GET /view/%s", channel)
      val msgs = mongo("freenodeHASH%s" format channel).find().limit(10)
      // TODO - Paging
      val ctx = List(
        "channel"  -> "#%s".format(channel),
        "messages" -> msgs
      )
      log.info("context: %s", ctx)
      Ok ~> Scalate(req, "view.jade", ctx: _*)
  }
}

object Server extends Logging {
  def main(args: Array[String]) {
    val http = unfiltered.jetty.Http(8080)

    http.filter(new ScalGoreWeb).run(/*{ svr => 
      unfiltered.util.Browser.open(http.url)
    },*/ { svr => 
      log.info("Shutting down server.")
    })
  }
}


// vim: set ts=2 sw=2 sts=2 et:
