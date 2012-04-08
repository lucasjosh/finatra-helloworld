package com.posterous.finatrahelloworld

import com.posterous.finatra.{FinatraApp, FinatraServer}
import org.jboss.netty.util.CharsetUtil.UTF_8
import org.apache.commons.fileupload._
import com.twitter.finagle.http.{Http, RichHttp, Request, Response}
import scala.collection.mutable._
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
        
        val multiParams = new ListBuffer[Tuple3[String, java.util.Map[String, String], ByteArrayOutputStream]]
        try {
          val multistream = new MultipartStream(input, boundary)
          var nextPart = multistream.skipPreamble
          while(nextPart){
            val paramParser = new ParameterParser
            val headers = paramParser.parse(multistream.readHeaders.toString, ';').asInstanceOf[java.util.Map[String,String]]
            val out = new ByteArrayOutputStream
            val name = headers.get("name").toString
            multistream.readBodyData(out)
            multiParams += Tuple3(name, headers, out)
            nextPart = multistream.readBoundary
          }
        } catch {
          case e: MultipartStream.MalformedStreamException => println("wrong") 
          case e: IOException => println("error") 
        }
        println(multiParams)
      }
    }
  }

  def main(args: Array[String]) {
    FinatraServer.register(HelloWorld)
    FinatraServer.start()
  }

}


