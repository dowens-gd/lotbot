package com.technophobia.lotbot.crawlr

import com.technophobia.lotbot.mock.VeryStaticInternet
import org.junit.runner.RunWith
import org.scalatest.{FeatureSpec, GivenWhenThen, Matchers}
import org.scalatest.junit.JUnitRunner

// look at scalatest eclipse plugin
// also funsuite
@RunWith(classOf[JUnitRunner])
class TestCrawlr extends FeatureSpec with GivenWhenThen with Matchers with VeryStaticInternet {

  def beforeEach() {
    println("beforeEach")
  }

  info("As a bot I can responed to HTTP response code appropriately")

  feature("GET") {

    scenario("Make request to index of simple site page") {

      Given("A site is up and page exists")
      Crawlr.main(Array("http://localhost:8889/index.html"))
    }
  }

    scenario("Make request to a page that doesn't exist") {

      Given("A site is up and a page that doesn't exists I don't fal in a heap")
      Crawlr.main(Array("http://localhost:8889/this-url-returns-404"))
  }
}