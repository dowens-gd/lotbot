package com.technophobia.lotbot.model

import org.jsoup.nodes.Document
import org.scala_tools.time.Imports._

/**
 * Created by dowens on 14/11/14.
 */
case class RequestResponse(lastTested :DateTime, httpStatus :String, url: String, parsedDoc: Option[Document])

object WorldWideWeb {

}


