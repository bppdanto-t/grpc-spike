package com.traveloka.spike.grpc.config

import io.grpc.ServerBuilder
import io.grpc.netty.shaded.io.grpc.netty.GrpcSslContexts
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder
import io.grpc.netty.shaded.io.netty.handler.ssl.SslContextBuilder
import io.grpc.netty.shaded.io.netty.handler.ssl.SslProvider
import org.lognet.springboot.grpc.GRpcServerBuilderConfigurer
import org.springframework.stereotype.Component
import java.io.File

@Component
class CustomGrpcServerBuilderConfigurer : GRpcServerBuilderConfigurer() {
  private val trustCertPath = "cert/ca.crt"
  private val certFilePath = "cert/server.crt"
  private val keyFilePath = "cert/server.pem"

  private fun getSSLContext(): SslContextBuilder {
    val classLoader: ClassLoader = this.javaClass.classLoader
    val sslContextBuilder: SslContextBuilder = SslContextBuilder.forServer(classLoader.getResourceAsStream(certFilePath),
        classLoader.getResourceAsStream(keyFilePath))
    return GrpcSslContexts.configure(sslContextBuilder, SslProvider.OPENSSL)
  }

  override fun configure(serverBuilder: ServerBuilder<*>?) {
    if (serverBuilder is NettyServerBuilder) {
      serverBuilder.sslContext(getSSLContext().build())
    }
  }
}