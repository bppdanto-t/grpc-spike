package com.traveloka.spike.grpc.service

import com.traveloka.spike.grpc.DemoProto
import com.traveloka.spike.grpc.GreeterGrpc
import io.grpc.stub.StreamObserver
import org.lognet.springboot.grpc.GRpcService
import org.slf4j.Logger
import org.springframework.beans.factory.annotation.Autowired

@GRpcService
class GreeterService : GreeterGrpc.GreeterImplBase() {
    @Autowired
    private lateinit var log: Logger

    override fun sayHello(request: DemoProto.HelloRequest?, responseObserver: StreamObserver<DemoProto.HelloReply>?) {
        val message = "Hello " + request!!.name + ", from 1st service"
        val replyBuilder = DemoProto.HelloReply.newBuilder().setMessage(message)
        responseObserver!!.onNext(replyBuilder.build())
        responseObserver.onCompleted()
        log.info("Returning $message")
    }
}