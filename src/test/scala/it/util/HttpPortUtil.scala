package it.util

import java.net.ServerSocket
import scala.util.Try

object HttpPortUtil {

  private val findNewOpenPort = 0
  private val errorDuringPortSearch = -1

  def findFreePort: Int =
    Try {
      val ss = new ServerSocket(findNewOpenPort)
      val port = ss.getLocalPort
      ss.close()
      port
    }.getOrElse(errorDuringPortSearch)

}
