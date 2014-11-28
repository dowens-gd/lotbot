package com.technophobia.lotbot.crawlr

import java.io.IOException
import java.net.URL

import com.technophobia.lotbot.model.RequestResponse
import com.typesafe.scalalogging.slf4j.LazyLogging
import org.jsoup.nodes.Element
import org.jsoup.{HttpStatusException, Jsoup}
import org.scala_tools.time.Imports._

import scala.collection.JavaConverters._
import scala.collection.immutable.HashSet
import scala.util.{Failure, Success, Try}

object Crawlr extends App with LazyLogging {

  var allToIndex : List[RequestResponse] = Nil

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
    allToIndex = allToIndex :::(urls.map(performGet(_)).toList)

  }
  logger.debug("I found  " + allToIndex.size + " things I thought I'd have a go at getting and here they are...")
  allToIndex.map(_.toString).foreach(logger.debug(_))

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
      .ignoreHttpErrors(false) // gives us a HttpStatusException if not 200
      .execute())

    connectionResponse match {
      case Success(s) =>
        return new RequestResponse(DateTime.now, s.statusCode.toString, url,
          Option(s.parse))
      case Failure(e) =>
        e match {
          case h: HttpStatusException => return new RequestResponse(DateTime.now,  h.getMessage, url, None)
          case ce: IOException => return new RequestResponse( DateTime.now,  ce.getClass.getName, url, None)
          case t => {
            logger.info("Failed to get HTTP response code from " + url)
            logger.debug("Got " + t.getClass + ": " + t.getMessage)
            throw t
          }
        }
    }
  }


  private def getHttpStatus(url: String): Seq[Element] = {

    val doc = Try(Jsoup.connect(url).userAgent("lotbot").get())
    if (doc.isFailure) {
      logger.info("Failed to get OK response from " + url)
      logger.debug("Got " + doc.failed.get.getMessage)
      return Nil
    }

    val hrefElements = doc.get.select("[href]")
    hrefElements.subList(0, hrefElements.size()).asScala
  }

  private def fullyQualify(url: String, seedUrl: String) = {
    if (url.startsWith("http://")) url else seedUrl + "/" + url
  }

}