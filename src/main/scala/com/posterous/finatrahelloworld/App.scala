package com.posterous.finatrahelloworld

import com.posterous.finatra.{FinatraApp, FinatraServer}
import com.capotej.finatra_core.MultipartItem

object App {

  class HelloWorld extends FinatraApp {

    get("/") { request =>
      response(body="hey")
    }

    post("/form") { request =>
      request.multiParams.get("foo") match {
        case Some(item) => response(body=item.data.toString)
        case None => response(status=500, body="incorrect params sent")
      }
    }

    get("/template") { request =>
      render(path="example.mustache", exports=Map("foo" -> "bar"))
    }

    get("/a/b/:c") { request =>
      response(body=request.params.get("c").getOrElse("none"))
    }

    get("/json") { request =>
      toJson(Map("foo" -> "bar"))
    }

    get("/formtest") { request =>
      render(path="formtest.mustache")
    }

    post("/formtest") { request =>
      response(body=request.params.get("foo").getOrElse("none"))
    }
  }

  def main(args: Array[String]) {
    val helloWorld = new HelloWorld
    FinatraServer.register(helloWorld)
    FinatraServer.start()
  }

}


