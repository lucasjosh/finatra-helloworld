package com.posterous.finatrahelloworld

import com.twitter.finatra.{Controller, FinatraServer}
import com.twitter.View

case class Tweet(status:String)

class TimelineView(val tweets:List[Tweet]) extends View {
  val template = "timeline.mustache"
}

object App {

  class HelloWorld extends Controller {

    def tweets = List(new Tweet("hey!"), new Tweet("lol"))

    get("/tweets.json") { request =>
      render.json(tweets)
    }

    get("/tweets") { request =>
      val tweetsView  = new TimelineView(tweets)

      render.view(tweetsView).ok
    }

    get("/:status") { request =>
      val statusCode = request.params("status").toInt

      render.nothing.status(statusCode)
    }

    get("/not_found") { request =>
      render.nothing.notFound
    }

    get("/headers") { request =>
      render.nothing.header("X-GitSHA", "1ecd6b1")
    }

  }

  def main(args: Array[String]) {
    val helloWorld = new HelloWorld

    FinatraServer.register(helloWorld)
    FinatraServer.start()
  }

}


