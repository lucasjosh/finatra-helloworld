package com.posterous.finatrahelloworld

import com.posterous.finatra.{FinatraApp, FinatraServer, LayoutHelper, LayoutHelperFactory}
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
      case class Thing(name: String)
      object TheThing {
        val foo = "bar"
        val list = List(new Thing("a"), new Thing("b"))
      }
      render(path="example.mustache", exports=TheThing)
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

    class MyLayoutHelper(yld: String) extends LayoutHelper(yld) {
      val hey = "there"
    }

    class MyFactory extends LayoutHelperFactory {
      override def apply(str: String) = {
        new MyLayoutHelper(str)
      }
    }

    FinatraServer.layoutHelperFactory = new MyFactory
    FinatraServer.register(helloWorld)
    FinatraServer.start()
  }

}


