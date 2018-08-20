package com.gonwan.springcloud.license.security;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .requestMatchers(EndpointRequest.to("info", "/v1/tools/eureka/services", "/v2/api-docs", "/swagger-ui.html")).permitAll()
                .requestMatchers(EndpointRequest.toAnyEndpoint()).hasRole("ACTUATOR")
                .antMatchers(HttpMethod.DELETE, "/v1/organizations/**").hasRole("ADMIN")
                .antMatchers("/v1/organizations/**").hasRole("USER");
    }

}
