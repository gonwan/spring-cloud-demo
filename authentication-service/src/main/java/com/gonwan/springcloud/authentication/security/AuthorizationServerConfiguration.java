package com.gonwan.springcloud.authentication.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.Arrays;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private TokenStore tokenStore;

    @Bean
    public UserDetailsService userDetailsService() {
        User.UserBuilder builder = User.builder();
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(builder.username("user").password("{noop}password1").roles("USER").build());
        manager.createUser(builder.username("admin").password("{noop}password2").roles("USER", "ADMIN").build());
        return manager;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        ProviderManager providerManager = new ProviderManager(Arrays.asList(provider));
        return providerManager;
    }

    /*
     * Define client details.
     * # curl -u eagleeye:thisissecret http://localhost:8901/oauth/token -X POST -d "grant_type=password&scope=webclient&username=user&password=password1"
     * {"access_token":"25c9b6bf-523a-4447-99c5-f9c718ebe813","token_type":"bearer","refresh_token":"fe4b80ea-70f3-4dc8-8a85-fd80e2181a28","expires_in":43199,"scope":"webclient"}
     * # curl -H "Authorization: Bearer 25c9b6bf-523a-4447-99c5-f9c718ebe813" http://localhost:8901/user
     * {"user":{"password":null,"username":"user","authorities":[{"authority":"ROLE_USER"}],"accountNonExpired":true,"accountNonLocked":true,"credentialsNonExpired":true,"enabled":true},"authorities":["ROLE_USER"]
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("eagleeye")  /* client name / application name */
                .secret("{noop}thisissecret")  /* secret / application password */
                .authorizedGrantTypes("password", "client_credentials", "refresh_token")
                .scopes("webclient", "mobileclient");
    }

    /*
     * Define security constraints on token endpoints.
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll()").checkTokenAccess("permitAll()")/*.allowFormAuthenticationForClients()*/;
    }

    /*
     * Define authorization, token endpoints & token services.
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .tokenStore(tokenStore)  /* default prefix: "access:"/"auth_to_access:" */
                .authenticationManager(authenticationManager())
                .userDetailsService(userDetailsService());
    }

    @Configuration
    static class DetailConfiguration {

        @Bean
        public PasswordEncoder passwordEncoder() {
            //return NoOpPasswordEncoder.getInstance();
            /*
             * Now the password looks like: "{bcrypt}" + BCryptPasswordEncoder.encode("rawPassword").
             */
            return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        }

        @Bean("tokenStore")
        public TokenStore redisTokenStore(RedisConnectionFactory redisConnectionFactory) {
            RedisTokenStore tokenStore = new RedisTokenStore(redisConnectionFactory);
            tokenStore.setPrefix("oauth2:");
            tokenStore.setSerializationStrategy(new Jackson2SerializationStrategy());  /* or jdk serializer by default. */
            return tokenStore;
        }

    }

}
