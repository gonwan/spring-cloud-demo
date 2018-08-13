package com.gonwan.springcloud.authentication.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

/*
 * We do not have @EnableWebSecurity, so it is not applied automatically.
 * The main purpose is to expose beans.
 */
@Configuration
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

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

    @Override /* for password grant??? */
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("john.carnell").password("password1").roles("USER")
                .and()
                .withUser("william.woodward").password("password2").roles("USER", "ADMIN");
    }

}
