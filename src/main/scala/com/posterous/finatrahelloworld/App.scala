package com.posterous.finatrahelloworld

import com.posterous.finatra.{FinatraApp, FinatraServer}

object App {
 
  object HelloWorld extends FinatraApp {
    get("/") { <marquee>hello world</marquee> }
  }

  def main(args: Array[String]) {
    FinatraServer.register(HelloWorld)
    FinatraServer.start()
  }

}


