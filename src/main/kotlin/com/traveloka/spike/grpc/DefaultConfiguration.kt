package com.traveloka.spike.grpc

import com.traveloka.spike.grpc.config.CustomGrpcServerBuilderConfigurer
import org.lognet.springboot.grpc.GRpcServerBuilderConfigurer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InjectionPoint
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class DefaultConfiguration: WebMvcConfigurer {
    /*@Bean
    fun grpcServerConfigurer(): GRpcServerBuilderConfigurer {
        return CustomGrpcServerBuilderConfigurer()
    }
*/
    @Bean
    @Scope("prototype")
    fun log(injectionPoint: InjectionPoint): Logger {
        return LoggerFactory.getLogger(injectionPoint.methodParameter?.containingClass
                ?: injectionPoint.field?.declaringClass)
    }
}