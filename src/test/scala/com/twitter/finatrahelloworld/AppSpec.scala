package com.twitter.finatrahelloword.test

import com.twitter.finatra.test.SpecHelper
import com.twitter.finatrahelloworld.App

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class AppSpec extends SpecHelper {

  def app = { new App.HelloWorld }

  "GET /tweets.json" should "respond 200" in {
    get("/tweets.json")
    response.code should equal (200)
  }

  "GET /tweets" should "respond 200" in {
    get("/tweets")
    response.code should equal (200)
    response.body should include ("lol")
  }

  "GET /status/401" should "respond 401" in {
    get("/status/401")
    response.code should equal (401)
  }

  "GET /not_found" should "respond 404" in {
    get("/not_found")
    response.code should equal (404)
  }

  "GET /headers" should "respond with headers" in {
    get("/headers")
    response.getHeader("X-GitSHA") should equal ("1ecd6b1")
  }
}