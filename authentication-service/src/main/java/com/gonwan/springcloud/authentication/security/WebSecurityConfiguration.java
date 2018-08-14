package com.gonwan.springcloud.authentication.security;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/*
 * We do not have @EnableWebSecurity, so it is not applied automatically.
 * The main purpose is to expose beans.
 */
@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        //return NoOpPasswordEncoder.getInstance();
        /*
         * Now the password looks like: "{bcrypt}" + BCryptPasswordEncoder.encode("rawPassword").
         */
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

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
                .authorizeRequests()
                .requestMatchers(EndpointRequest.toAnyEndpoint()).hasRole("ACTUATOR")
                .antMatchers("/**").permitAll()
                .and()
                .httpBasic();
    }

    @Override /* for password grant */
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .passwordEncoder(NoOpPasswordEncoder.getInstance())  /* or use default @Autowired one */
                .withUser("user").password("password1").roles("USER")
                .and()
                .withUser("admin").password("password2").roles("USER", "ADMIN");
    }

}
