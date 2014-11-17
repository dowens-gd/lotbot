package com.technophobia.lotbot.crawlr

import java.net.URL

import com.technophobia.lotbot.model.RequestResponse
import com.typesafe.scalalogging.slf4j.LazyLogging
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.scala_tools.time.Imports._

import scala.collection.JavaConverters._
import scala.collection.immutable.HashSet
import scala.util.Try

object Crawlr extends App with LazyLogging {

  logger.info("Welcome to Crawlr")
  for (seedUrl <- args) {
    val url = new URL(seedUrl)
    val siteUrl = seedUrl.stripSuffix(url.getPath)

    var urls = HashSet(seedUrl, siteUrl)

    logger.debug("Indexing from " + seedUrl)
    for (hrefelement <- getHrefElements(seedUrl)) {
      val url = fullyQualify(hrefelement.attr("href"), siteUrl)
      urls += url
    }

    logger.debug("Discovered " + urls.size + " distinct URLs to attempt indexing")
    urls.foreach(logger.debug(_))

    logger.debug("GETting urls")
    urls.foreach(performGet(_))
  }
  logger.info("Bye!")


  private def getHrefElements(url: String): Seq[Element] = {

    val requestResponse = performGet(url)
    val parsedDoc = requestResponse.parsedDoc
    parsedDoc match {
      case Some(doc) =>
        return doc.select("[href]").asScala
      case None =>
        return Nil
    }

  }


  private def performGet(url: String): RequestResponse = {

    val connectionResponse = Try(Jsoup.connect(url)
      .timeout(1000)
      .userAgent("lotbot")
      .followRedirects(false)
      .ignoreHttpErrors(true)
      .execute())

    if (connectionResponse.isFailure) {
      logger.info("Failed to get response from " + url)
      logger.debug("Got " + connectionResponse.failed.get.getMessage)
      return new RequestResponse(url, 0, DateTime.now, None)
    }
    return new RequestResponse(url, connectionResponse.get.statusCode(), DateTime.now,
      Option(connectionResponse.get.parse()))
  }


  private def getHttpStatus(url: String): Seq[Element] = {

    val doc = Try(Jsoup.connect(url).userAgent("lotbot").get())
    if (doc.isFailure) {
      logger.info("Failed to get OK response from " + url)
      logger.debug("Got " + doc.failed.get.getMessage)
      return Nil
    }

    // hrefs
    val hrefElements = doc.get.select("[href]")
    hrefElements.subList(0, hrefElements.size()).asScala
  }

  private def fullyQualify(url: String, seedUrl: String) = {
    // hrefs
    if (url.startsWith("http://")) url else seedUrl + "/" + url
  }

}