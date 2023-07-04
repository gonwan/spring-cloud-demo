package com.gonwan.springcloud.authentication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableWebSecurity
@SpringBootApplication
public class AuthenticationNextApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthenticationNextApplication.class, args);
    }

}
