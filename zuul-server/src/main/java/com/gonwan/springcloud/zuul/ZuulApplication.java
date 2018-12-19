package com.gonwan.springcloud.zuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import io.micrometer.core.annotation.Timed;

@SpringBootApplication
@EnableZuulProxy
@RestController
public class ZuulApplication {

    @LoadBalanced
    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Timed(value = "hello")
    @GetMapping(value = "/hello", produces = "application/json")
    public String hello() {
        return "hello";
    }

    @Timed(value = "hello2")
    @GetMapping(value = "/hello2", produces = "application/json")
    public String hello2() {
        return "hello2";
    }

    public static void main(String[] args) {
        SpringApplication.run(ZuulApplication.class, args);
    }

}
