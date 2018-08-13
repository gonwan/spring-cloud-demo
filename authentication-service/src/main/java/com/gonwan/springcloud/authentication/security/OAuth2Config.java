package com.gonwan.springcloud.authentication.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;

@Configuration
public class OAuth2Config extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    /*
     * # curl -u eagleeye:thisissecret http://localhost:8901/auth/oauth/token -X POST -d "grant_type=password&scope=webclient&username=john.carnell&password=password1"
     * {"access_token":"25c9b6bf-523a-4447-99c5-f9c718ebe813","token_type":"bearer","refresh_token":"fe4b80ea-70f3-4dc8-8a85-fd80e2181a28","expires_in":43199,"scope":"webclient"}
     * # curl -H "Authorization: Bearer 25c9b6bf-523a-4447-99c5-f9c718ebe813" http://localhost:8901/auth/user
     * {"user":{"password":null,"username":"john.carnell","authorities":[{"authority":"ROLE_USER"}],"accountNonExpired":true,"accountNonLocked":true,"credentialsNonExpired":true,"enabled":true},"authorities":["ROLE_USER"]
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("eagleeye")  /* client name / application name */
                .secret("thisissecret")  /* secret / application password */
                .authorizedGrantTypes("password", "refresh_token", "client_credentials")
                .scopes("webclient", "mobileclient");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService);
    }

}
