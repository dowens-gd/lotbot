package com.technophobia.lotbot.model

import org.jsoup.nodes.Document
import org.scala_tools.time.Imports._

/**
 * Created by dowens on 14/11/14.
 */
case class RequestResponse(url: String, httpStatus :Int, lastTested :DateTime, parsedDoc: Option[Document])

object WorldWideWeb {

}


