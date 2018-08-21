package com.gonwan.springcloud.authentication.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

/*
 * Config security and expose beans.
 */
@Order(1)
@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;

    /* Expose a {@link AuthenticationManager} created from {@link #configure(AuthenticationManagerBuilder)} as a bean. */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /* Expose a {@link UserDetailsService} created from {@link #configure(AuthenticationManagerBuilder)} as a bean. */
    @Bean
    @Override
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return super.userDetailsServiceBean();
    }

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

    @Override /* for password grant */
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .inMemoryAuthentication()
                .withUser("user").password(passwordEncoder.encode("password1")).roles("USER", "ACTUATOR")
                .and()
                .withUser("admin").password(passwordEncoder.encode("password2")).roles("USER", "ADMIN");
    }

}
