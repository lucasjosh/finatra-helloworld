package com.posterous.finatrahelloworld

import com.posterous.finatra.{FinatraApp, FinatraServer}
import org.jboss.netty.util.CharsetUtil.UTF_8
import org.apache.commons.fileupload._
import com.twitter.finagle.http.{Http, RichHttp, Request, Response}
import java.io._

class FakeServlet(request:Request) {

  def getReader() = {
    new BufferedReader(new StringReader(request.getContent.toString(UTF_8))) 
  }

  def fakeit() = {
    this.asInstanceOf[javax.servlet.http.HttpServletRequest] 
  }

  override def toString() = {
    request.toString 
  }

}


object App {
 
  object HelloWorld extends FinatraApp {
    post("/") { 
      val ctype = request.headers.getOrElse("Content-Type", null)
      if(ctype != null){
        val boundaryIndex = ctype.indexOf("boundary=");
        val boundary = ctype.substring(boundaryIndex + 9).getBytes
        val input = new ByteArrayInputStream(request.getContent.toString(UTF_8).getBytes) 
        
        //val multiParams = new scala.collection.mutable.Map[Tuple[]]
        var output = new ByteArrayOutputStream
        try {
          val multistream = new MultipartStream(input, boundary)
          var nextPart = multistream.skipPreamble()
          while(nextPart){
            val header = multistream.readHeaders
            multistream.readBodyData(output)
            nextPart = multistream.readBoundary
            println(header)
            //println(output)
          }
        } catch {
          case e: MultipartStream.MalformedStreamException => println("wrong") 
          case e: IOException => println("error") 
        }
      }
    }
  }

  def main(args: Array[String]) {
    FinatraServer.register(HelloWorld)
    FinatraServer.start()
  }

}


