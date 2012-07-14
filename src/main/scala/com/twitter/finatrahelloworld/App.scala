package com.twitter.finatrahelloworld

import com.twitter.finatra.{Controller, FinatraServer}
import com.twitter.finatra.View

case class Tweet(status:String)

class TimelineView(val tweets:List[Tweet]) extends View {
  val template = "timeline.mustache"
}

object App {

  class HelloWorld extends Controller {

    def tweets = List(new Tweet("hey!"), new Tweet("lol"))

    //curl -F "foo=bar" http://localhost:7070/multi
    post("/multi") { request =>
      render.plain(request.multiParams("foo").data)
    }

    get("/cookies") { request =>
      render.json(request.cookies).header("Content-Type", "text/html")
    }

    get("/tweets.json") { request =>
      render.json(tweets)
    }

    get("/tweets") { request =>
      val tweetsView  = new TimelineView(tweets)

      render.view(tweetsView)
    }

    get("/status/:status") { request =>
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


