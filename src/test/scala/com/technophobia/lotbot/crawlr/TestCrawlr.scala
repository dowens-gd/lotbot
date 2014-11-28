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

  info("As a bot I can respond to network problems and HTTP response codes appropriately")

  feature("GET") {

    scenario("Make request to index of simple site page") {

      Given("A site is up and page exists")
      Crawlr.main(Array("http://localhost:8889/index.html"))
    }

    scenario("Making a request to non-existant server") {

      Given("A a non-existant server make sure I don't fall in a heap")
      Crawlr.main(Array("http://rameses-niblick-the-third-kerplunk-kerplunk-whoops-wheres-my-thribble"))
    }

    scenario("Making a request on incorrect port") {

      Given("A server is up but not listening on the port requested I don't fall in a heap")
      Crawlr.main(Array("http://localhost:666"))
    }

    scenario("Make request to a page that doesn't exist") {

        Given("A site is up and a page that doesn't exists I don't fall in a heap")
        Crawlr.main(Array("http://localhost:8889/this-url-returns-404"))
    }
  }
}