package com.gonwan.springcloud.authentication.security;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

//@Configuration
//@EnableAuthorizationServer  /* @EnableResourceServer is used in client to protect resources. */
public class JwtAuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    @Autowired
    private TokenEnhancer jwtTokenEnhancer;

    /*
     * Define client details.
     * # curl -u eagleeye:thisissecret http://localhost:8901/auth/oauth/token -X POST -d "grant_type=password&scope=webclient&username=user&password=password1"
     * {"access_token":"25c9b6bf-523a-4447-99c5-f9c718ebe813","token_type":"bearer","refresh_token":"fe4b80ea-70f3-4dc8-8a85-fd80e2181a28","expires_in":43199,"scope":"webclient"}
     * # curl -H "Authorization: Bearer 25c9b6bf-523a-4447-99c5-f9c718ebe813" http://localhost:8901/auth/user
     * {"user":{"password":null,"username":"user","authorities":[{"authority":"ROLE_USER"}],"accountNonExpired":true,"accountNonLocked":true,"credentialsNonExpired":true,"enabled":true},"authorities":["ROLE_USER"]
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("eagleeye")  /* client name / application name */
                .secret(passwordEncoder.encode("thisissecret"))  /* secret / application password */
                .authorizedGrantTypes("password", "client_credentials", "refresh_token")
                .scopes("webclient", "mobileclient");
    }

    /*
     * Define security constraints on token endpoints.
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        super.configure(security);
    }

    /*
     * Define authorization, token endpoints & token services.
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(jwtTokenEnhancer, jwtAccessTokenConverter));
        endpoints.tokenStore(tokenStore)
                .accessTokenConverter(jwtAccessTokenConverter)
                .tokenEnhancer(tokenEnhancerChain)
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService);
    }

    @Configuration
    static class DetailConfiguration {

        @Bean
        public TokenStore tokenStore() {
            return new JwtTokenStore(jwtAccessTokenConverter());
        }

        @Bean
        @Primary
        public DefaultTokenServices tokenServices() {
            DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
            defaultTokenServices.setTokenStore(tokenStore());
            defaultTokenServices.setSupportRefreshToken(true);
            return defaultTokenServices;
        }


        @Bean
        public JwtAccessTokenConverter jwtAccessTokenConverter() {
            JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
            converter.setSigningKey("jwts1gningkey");
            return converter;
        }

        @Bean
        public TokenEnhancer jwtTokenEnhancer() {
            return new TokenEnhancer() {
                @Override
                public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
                    Map<String, Object> additionalInfo = new HashMap<>();
                    additionalInfo.put("organizationId", "myorg");
                    ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
                    return accessToken;
                }
            };
        }
        
        @Bean
        public PasswordEncoder passwordEncoder() {
            //return NoOpPasswordEncoder.getInstance();
            /*
             * Now the password looks like: "{bcrypt}" + BCryptPasswordEncoder.encode("rawPassword").
             */
            return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        }

        @Bean
        public TokenStore redisTokenStore(RedisConnectionFactory redisConnectionFactory) {
            return new RedisTokenStore(redisConnectionFactory);  /* Jdk serializer by default and not so easy to change. */
        }

    }

}
