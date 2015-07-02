import java.io.{BufferedWriter, File, FileWriter}
import java.time.Instant

import com.twitter.finagle.{Http, Service}
import com.twitter.io.Charsets
import com.twitter.server.TwitterServer
import com.twitter.util.{Await, Future}
import org.jboss.netty.buffer.ChannelBuffers.copiedBuffer
import org.jboss.netty.handler.codec.http._

import scala.collection.JavaConversions._

object BasicServer extends TwitterServer {
  val service = new Service[HttpRequest, HttpResponse] {
    def apply(request: HttpRequest) = {

      val headers = request.headers()
      val headerKeys = headers.names()

      val file = new File("./foo.txt")
      val bw = new BufferedWriter(new FileWriter(file, true))

      bw.append("Method: " + request.getMethod() + System.lineSeparator())

      bw.append("URL: " + request.getUri() + System.lineSeparator())

      bw.append("Headers: " + System.lineSeparator())

      headerKeys.foreach(key => bw.append("key: " + key + " value: " + headers.get(key) + System.lineSeparator()))

      val content = request.getContent().toString(Charsets.Utf8)

      bw.append("Content: " + content + System.lineSeparator())
      bw.close()

      val response =
        new DefaultHttpResponse(request.getProtocolVersion, HttpResponseStatus.OK)
      response.setContent(copiedBuffer("Logged at: " + Instant.now().toString(), Charsets.Utf8))
      Future.value(response)
    }
  }

  def main() {
    val port = args(0)
    val server = Http.serve(":" + port, service)
    onExit {
      server.close()
    }
    Await.ready(server)
  }
}