package com.traveloka.spike.grpc.controller

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/")
class HealthCheckController {
  @GetMapping("healthcheck")
  fun getHealth(): ResponseEntity<Any> {
    return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body("{data:\"healthy euy\"}")
  }
}