package com.technophobia.lotbot.crawlr

import org.junit.runner.RunWith
import org.scalatest.FeatureSpec
import org.scalatest.GivenWhenThen
import org.scalatest.Matchers
import org.scalatest.junit.JUnitRunner
import org.scalatest.BeforeAndAfterAll
import sun.net.www.http.HttpClient
import java.net.URL
import com.technophobia.lotbot.mock.VeryStaticInternet
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
      val notABotYet = HttpClient.New(new URL("http://localhost:8889/index.html"))

    }
  }
}