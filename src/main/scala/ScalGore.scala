/**
 * ScalGore simple #akka IRC Bot.
 *
 * Brendan W. McAdams <bwmcadams@evilmonkeylabs.com>
 */
package net.evilmonkeylabs.scalgore

import org.jibble.pircbot._
import net.lag.configgy.Configgy
import se.scalablesolutions.akka.actor._
import se.scalablesolutions.akka.util._

object ScalGore extends Logging {

  def main(args: Array[String]) {
    val bot = new IrcBot("ScalGore", true, "irc.freenode.net", "##akkaTest")
  }
}

// vim: set ts=2 sw=2 sts=2 et:
