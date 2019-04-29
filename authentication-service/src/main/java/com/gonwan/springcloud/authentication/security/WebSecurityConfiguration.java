package com.gonwan.springcloud.authentication.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.util.StringUtils;

import java.util.List;

/*
 * See: https://docs.spring.io/spring-security/site/docs/current/reference/html/jc.html#multiple-httpsecurity
 */
@Order(1)
@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private SecurityProperties securityProperties;

    /*
     * See migration guide: https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-Security-2.0
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .antMatcher("/actuator/**")
                .authorizeRequests()
                    .requestMatchers(EndpointRequest.to("info", "health")).permitAll()
                    .requestMatchers(EndpointRequest.toAnyEndpoint()).hasRole("ACTUATOR")
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
