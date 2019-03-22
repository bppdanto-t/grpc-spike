import com.traveloka.spike.grpc.DemoProto
import com.traveloka.spike.grpc.GreeterGrpc
import io.grpc.ManagedChannel
import io.grpc.StatusRuntimeException
import io.grpc.netty.shaded.io.grpc.netty.GrpcSslContexts
import io.grpc.netty.shaded.io.grpc.netty.NegotiationType
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder
import io.grpc.netty.shaded.io.netty.handler.ssl.SslContextBuilder
import io.grpc.netty.shaded.io.netty.handler.ssl.SslProvider
import java.io.File
import java.util.concurrent.TimeUnit
import java.util.logging.Level
import java.util.logging.Logger

class GrpcClient
internal constructor(private val channel: ManagedChannel) {
  private val blockingStub: GreeterGrpc.GreeterBlockingStub = GreeterGrpc.newBlockingStub(channel)

  /** Construct client connecting to HelloWorld server at `host:port`.  */
  constructor(host: String, port: Int) : this(getChannel(host, port))

  @Throws(InterruptedException::class)
  fun shutdown() {
    channel.shutdown().awaitTermination(5, TimeUnit.SECONDS)
  }

  /** Say hello to server.  */
  fun greet(name: String) {
    logger.log(Level.INFO, "Will try to greet {0}...", name)
    val request = DemoProto.HelloRequest.newBuilder().setName(name).build()
    val response: DemoProto.HelloReply = try {
      blockingStub.sayHello(request)
    } catch (e: StatusRuntimeException) {
      logger.log(Level.WARNING, "RPC failed: {0}", e.status)
      return
    }

    logger.info("Greeting: ${response.message}")
  }

  companion object {
    private val logger = Logger.getLogger(GrpcClient::class.java.name)
    private val trustCertPath = "ca.crt"
    private val clientCertPath = "client.crt"
    private val clientKeyPath = "client.pem"

    private fun getChannel(host: String, port: Int): ManagedChannel {
      return NettyChannelBuilder.forAddress(host, port)
          // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
          // needing certificates.
          .negotiationType(NegotiationType.TLS)
          .sslContext(getSSLContext().build())
          .keepAliveWithoutCalls(true)
          .keepAliveTime(150, TimeUnit.SECONDS)
          .overrideAuthority("localhost")
          //.usePlaintext()
          .build()
    }

    private fun getSSLContext(): SslContextBuilder {
      val classLoader: ClassLoader = this::class.java.classLoader
      val sslContextBuilder: SslContextBuilder = SslContextBuilder.forClient()
      sslContextBuilder.trustManager(File(classLoader.getResource(trustCertPath).file))
      return GrpcSslContexts.configure(sslContextBuilder, SslProvider.OPENSSL)
    }

    /**
     * Greet server. If provided, the first element of `args` is the name to use in the
     * greeting.
     */
    @Throws(Exception::class)
    @JvmStatic
    fun main(args: Array<String>) {
      val client = GrpcClient("3.1.135.192", 6565)
      //val client = GrpcClient("13.251.197.128", 6565)
      //val client = GrpcClient("grpc-test-clb-1403437984.ap-southeast-1.elb.amazonaws.com", 6565)
      //val client = GrpcClient("grpc-test-alb-1894363091.ap-southeast-1.elb.amazonaws.com", 6565)
      //val client = GrpcClient("54.254.215.45", 6565)
      //val client = GrpcClient("54.169.229.148", 6565)
      try {
        /* Access a service running on the local machine on port 50051 */
        val user = if (args.size > 0) "world" else "world"
        client.greet(user)
      } finally {
        client.shutdown()
      }
    }
  }
}