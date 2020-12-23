package com.gonwan.springcloud.gateway;

import io.netty.channel.ChannelOption;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.netty.NettyServerCustomizer;
import org.springframework.context.annotation.Bean;

import java.time.Duration;

@SpringBootApplication
public class GatewayApplication {

    @Bean
    public NettyServerCustomizer nettyServerCustomizer() {
        return httpServer -> {
            httpServer.childOption(ChannelOption.SO_KEEPALIVE, true);
            httpServer.idleTimeout(Duration.ofMinutes(10));
            return httpServer;
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

}
