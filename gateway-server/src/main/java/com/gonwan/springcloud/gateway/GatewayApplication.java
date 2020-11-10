package com.gonwan.springcloud.gateway;

import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.netty.NettyServerCustomizer;
import org.springframework.context.annotation.Bean;
import reactor.netty.channel.BootstrapHandlers;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class GatewayApplication {

    @Bean
    public NettyServerCustomizer nettyServerCustomizer() {
        return httpServer -> httpServer.tcpConfiguration(tcpServer -> {
            tcpServer = tcpServer.option(ChannelOption.SO_KEEPALIVE, true);
            /*
             * We are modifying child handler, use doOnBind() instead of doOnConnection().
             */
            tcpServer = tcpServer.doOnBind(serverBootstrap ->
                    BootstrapHandlers.updateConfiguration(serverBootstrap, "channelIdle", (connectionObserver, channel) -> {
                        ChannelPipeline pipeline = channel.pipeline();
                        pipeline.addLast(new ReadTimeoutHandler(10, TimeUnit.MINUTES));
                        pipeline.addLast(new WriteTimeoutHandler(10, TimeUnit.MINUTES));
                    }));
            return tcpServer;
        });
    }

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

}
