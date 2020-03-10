package com.gonwan.springcloud.license.security;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.stereotype.Component;

@Component
class UserInfoOpaqueTokenIntrospector implements OpaqueTokenIntrospector {

    private OAuth2UserService oauth2UserService;
    private ClientRegistration clientRegistration;
    private String userNameAttributeName;

    @Autowired
    public UserInfoOpaqueTokenIntrospector(ClientRegistrationRepository registrationRepository) {
        oauth2UserService = new DefaultOAuth2UserService();
        clientRegistration = registrationRepository.findByRegistrationId("sc-provider");
        userNameAttributeName = clientRegistration.getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
    }

    @Override
    public OAuth2AuthenticatedPrincipal introspect(String token) {
        OAuth2AccessToken oauth2AccessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, token, Instant.now(), Instant.now().plusSeconds(12*60*60));
        OAuth2UserRequest oauth2UserRequest = new OAuth2UserRequest(clientRegistration, oauth2AccessToken);
        OAuth2User ou = oauth2UserService.loadUser(oauth2UserRequest);
        List<String> authorities = ou.getAttribute("authorities");
        List<SimpleGrantedAuthority> simpleGrantedAuthorities = authorities.stream()
                .map(x -> new SimpleGrantedAuthority(x))
                .collect(Collectors.toList());
        return new DefaultOAuth2User(simpleGrantedAuthorities, ou.getAttributes(), userNameAttributeName);
    }

}

@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers(HttpMethod.DELETE, "/v1/organizations/**").hasRole("ADMIN")
                .antMatchers("/v1/organizations/**").hasRole("USER")
                .and()
            .oauth2ResourceServer().opaqueToken();
    }

}
