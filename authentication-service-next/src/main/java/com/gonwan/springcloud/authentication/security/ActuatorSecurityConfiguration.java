package com.gonwan.springcloud.authentication.security;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.info.InfoEndpoint;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.util.StringUtils;

import java.util.List;

//@Configuration
public class ActuatorSecurityConfiguration {

    @Bean("actuatorUserDetailsService")
    public UserDetailsService actuatorUserDetailsService(SecurityProperties securityProperties) {
        SecurityProperties.User user = securityProperties.getUser();
        List<String> roles = user.getRoles();
        return new InMemoryUserDetailsManager(User.withUsername(user.getName())
                .password("{noop}" + user.getPassword())
                .roles(StringUtils.toStringArray(roles))
                .build());
    }

    @Order(2)
    @Bean
    public SecurityFilterChain actuator(HttpSecurity http, @Qualifier("actuatorUserDetailsService") UserDetailsService userDetailsService) throws Exception {
        http
            .userDetailsService(userDetailsService) /* user/pass in application.yml */
            .securityMatcher(EndpointRequest.toAnyEndpoint())
            .authorizeHttpRequests((authorize) -> authorize
                .requestMatchers(EndpointRequest.to(HealthEndpoint.class, InfoEndpoint.class)).permitAll()
                .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults());
        return http.build();
    }

}
