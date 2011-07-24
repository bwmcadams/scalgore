package net.evilmonkeylabs.scalgore
package web

import com.mongodb.casbah.Imports._
import com.mongodb.casbah.commons.Logging 
import unfiltered.request._
import unfiltered.response._
import unfiltered.scalate._

class ScalGoreWeb extends unfiltered.filter.Plan with Logging {
  
  def intent = {
    case req @ GET(Path(uri)) => {
      log.info("GET %s", uri)
      Ok ~> Scalate(req, "view.jade")
    }
    case req @ _ => {
      log.info("GET /")
      Ok ~> Scalate(req, "root.jade")
    }
  }
}

object Server extends Logging {
  def main(args: Array[String]) {
    val http = unfiltered.jetty.Http(8080)

    http.context("/css") { 
      _.resources(new java.net.URL(getClass().getResource("/www/css"), "."))
    }.context("/gfx") {
      _.resources(new java.net.URL(getClass().getResource("/www/gfx"), "."))
    }.context("/js") {
      _.resources(new java.net.URL(getClass().getResource("/www/js"), "."))
    }.filter(new ScalGoreWeb).run({ svr => 
      unfiltered.util.Browser.open(http.url)
    }, { svr => 
      log.info("Shutting down server.")
    })
  }
}


// vim: set ts=2 sw=2 sts=2 et:
