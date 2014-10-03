//
// Adapted from sample at  
// https://raw.githubusercontent.com/mashupbots/socko/master/socko-examples/src/main/scala/org/mashupbots/socko/examples/benchmark/BenchmarkApp.scala
//

package com.technophobia.lotbot.mock

import java.io.File
import java.io.FileOutputStream
import org.mashupbots.socko.events.HttpResponseStatus
import org.mashupbots.socko.handlers.StaticContentHandler
import org.mashupbots.socko.handlers.StaticContentHandlerConfig
import org.mashupbots.socko.handlers.StaticFileRequest
import org.mashupbots.socko.infrastructure.IOUtil
import org.mashupbots.socko.routes.GET
import org.mashupbots.socko.routes.HttpRequest
import org.mashupbots.socko.routes.Path
import org.mashupbots.socko.routes.Routes
import org.mashupbots.socko.webserver.WebServer
import org.mashupbots.socko.webserver.WebServerConfig
import com.typesafe.config.ConfigFactory
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.actorRef2Scala
import akka.routing.FromConfig
import io.netty.util.CharsetUtil
import org.mashupbots.socko.routes.POST
import scala.reflect.io.Directory
import org.scalatest.Suite
import org.scalatest.BeforeAndAfterAll

trait SecondVeryStaticSite extends Suite with BeforeAndAfterAll {

  private val rootSiteDirectory = new File("src/test/resources/substeps").getAbsolutePath()
  private val port = 8890
  private val staticContentHandlerConfig = StaticContentHandlerConfig(Seq(rootSiteDirectory))
  private val webServer : WebServer = new WebServer(WebServerConfig(port=port), routes, actorSystem)

  override def beforeAll() {   
    webServer.start()
  }
  
  override def afterAll() {
    webServer.stop();
  } 
  
  //
  // STEP #1 - Define Actors and Start Akka
  //
  // We are going to start StaticContentHandler actor as a router. There will be 20 instances using a max of 6 threads.
  // Some basic benchmarking indicates that "thread-pool-executor" is better than "fork-join-executor" for
  // StaticContentHandler.
  //
  val actorConfig = """
	my-pinned-dispatcher {
	  type=PinnedDispatcher
	  executor=thread-pool-executor
	}
	my-dispatcher {
	  # Dispatcher is the name of the event-based dispatcher
	  type = Dispatcher
	  # What kind of ExecutionService to use
	  executor = "thread-pool-executor"
	  # Configuration for the fork join pool
	  thread-pool-executor {
	    # Min number of threads to cap factor-based parallelism number to
	    parallelism-min = 25
	    # Parallelism (threads) ... ceil(available processors * factor)
	    parallelism-factor = 2.0
	    # Max number of threads to cap factor-based parallelism number to
	    parallelism-max = 6
	  }
	  # Throughput defines the maximum number of messages to be
	  # processed per actor before the thread jumps to the next actor.
	  # Set to 1 for as fair as possible.
	  throughput = 100
	} 
    akka{
	  loggers = ["akka.event.slf4j.Slf4jLogger"]
	  loglevel=DEBUG
	  actor {
	    deployment {
	      /static-file-router {
	        router = round-robin
	        nr-of-instances = 20
	      }
	    }
	  }
	}"""

  val actorSystem = ActorSystem("substepsSiteActorSystem", ConfigFactory.parseString(actorConfig))
  val staticContentHandlerRouter = actorSystem.actorOf(Props(new StaticContentHandler(staticContentHandlerConfig))
    .withRouter(FromConfig()).withDispatcher("my-dispatcher"), "static-file-router")

  //
  // STEP #2 - Define Routes
  //
  val routes = Routes({
    case HttpRequest(request) => request match {
      case GET() => {
        staticContentHandlerRouter ! new StaticFileRequest(request, new File(rootSiteDirectory, request.nettyHttpRequest.getUri()))
      }
    }
  })

  //
  // STEP #3 - Start and Stop Socko Web Server
  //
  def main(args: Array[String]) {
    // Start web server
    val webServer = new WebServer(WebServerConfig(port=port), routes, actorSystem)
    Runtime.getRuntime.addShutdownHook(new Thread {
      override def run {
        webServer.stop()
      }
    })
    webServer.start()
    
    System.out.println("Content directory is " + rootSiteDirectory)
  }

}