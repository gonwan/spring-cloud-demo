package com.gonwan.springcloud.license.security;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.info.InfoEndpoint;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.util.StringUtils;

import java.util.List;

@Configuration
public class SecurityConfiguration {

    /* copied form UserDetailsServiceAutoConfiguration */
    @Bean
    public UserDetailsService userDetailsService(SecurityProperties securityProperties) {
        SecurityProperties.User user = securityProperties.getUser();
        List<String> roles = user.getRoles();
        return new InMemoryUserDetailsManager(User.withUsername(user.getName())
                .password("{noop}" + user.getPassword())
                .roles(StringUtils.toStringArray(roles))
                .build());
    }

    @Order(1)
    @Bean
    public SecurityFilterChain resourceWebFilterChain(HttpSecurity http) throws Exception {
        http
            .antMatcher("/v1/**")
            .authorizeRequests()
                .antMatchers(HttpMethod.DELETE, "/v1/organizations/**").hasRole("ADMIN")
                .antMatchers("/v1/organizations/**").hasRole("USER")
                .and()
            .oauth2ResourceServer().opaqueToken();
        return http.build();
    }

    @Bean
    public SecurityFilterChain actuatorWebFilterChain(HttpSecurity http, UserDetailsService userDetailsService) throws Exception {
        http
            .userDetailsService(userDetailsService) /* user/pass in application.yml */
            .requestMatcher(EndpointRequest.toAnyEndpoint())
            .authorizeRequests()
                .requestMatchers(EndpointRequest.to(HealthEndpoint.class, InfoEndpoint.class)).permitAll()
                .anyRequest().authenticated()
                .and()
            .httpBasic();
        return http.build();
    }

}
