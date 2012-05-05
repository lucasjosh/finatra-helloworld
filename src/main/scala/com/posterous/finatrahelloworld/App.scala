package com.posterous.finatrahelloworld

import com.posterous.finatra.{FinatraApp, FinatraServer}

object App {

  class HelloWorld extends FinatraApp {
    get("/") { request =>
      response("hey")
    }

  }

  def main(args: Array[String]) {
    val helloWorld = new HelloWorld
    FinatraServer.register(helloWorld)
    FinatraServer.start()
  }

}


