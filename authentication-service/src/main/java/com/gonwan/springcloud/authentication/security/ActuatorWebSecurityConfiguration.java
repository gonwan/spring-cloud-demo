package com.gonwan.springcloud.authentication.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.info.InfoEndpoint;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.util.StringUtils;

/*
 * See: https://docs.spring.io/spring-security/site/docs/current/reference/html/jc.html#multiple-httpsecurity
 */
@Order(1)
@Configuration
public class ActuatorWebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private SecurityProperties securityProperties;

    /*
     * See migration guide: https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-Security-2.0
     * See: org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        List<String> roles = securityProperties.getUser().getRoles();
        http
            .requestMatcher(EndpointRequest.toAnyEndpoint())
            .authorizeRequests()
                .requestMatchers(EndpointRequest.to(HealthEndpoint.class, InfoEndpoint.class)).permitAll()
                .anyRequest().hasAnyRole(StringUtils.toStringArray(roles))
                .and()
            .httpBasic();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        SecurityProperties.User user = securityProperties.getUser();
        List<String> roles = user.getRoles();
        auth
            .inMemoryAuthentication()
                .withUser(user.getName())
                .password("{noop}" + user.getPassword())
                .roles(StringUtils.toStringArray(roles));
    }

}
