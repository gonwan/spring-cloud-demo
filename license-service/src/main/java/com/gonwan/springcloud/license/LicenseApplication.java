package com.gonwan.springcloud.license;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication
@EnableEurekaClient
@EnableResourceServer
@EnableCircuitBreaker
@EnableCaching
@EnableOAuth2Client  // see: https://github.com/spring-projects/spring-security-oauth/issues/1275 & OAuth2RestOperationsConfiguration
public class LicenseApplication {

    // also see: https://stackoverflow.com/questions/40406858/configuring-spring-oauth2-client-for-client-credentials-flow
    // security:
    //   oauth2:
    //     client:
    //       grant-type: client_credentials
    @LoadBalanced
    @Bean
    @ConditionalOnProperty(prefix = "eureka", name = "client.enabled", havingValue = "true", matchIfMissing = true)
    public OAuth2RestTemplate lbOauth2RestTemplate(@Qualifier("oauth2ClientContext") OAuth2ClientContext oauth2ClientContext, OAuth2ProtectedResourceDetails details) {
        return new OAuth2RestTemplate(details, oauth2ClientContext);
    }

    @Bean
    @ConditionalOnProperty(prefix = "eureka", name = "client.enabled", havingValue = "false", matchIfMissing = false)
    public OAuth2RestTemplate oauth2RestTemplate(@Qualifier("oauth2ClientContext") OAuth2ClientContext oauth2ClientContext, OAuth2ProtectedResourceDetails details) {
        return new OAuth2RestTemplate(details, oauth2ClientContext);
    }

    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new GenericJackson2JsonRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }

    /*
     * Required for spring boot 2.0, or default JDK serializer are used.
     * See: RedisCacheConfiguration#cacheManager().
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration
                .defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()));
        return RedisCacheManager.builder(connectionFactory).cacheDefaults(cacheConfiguration).build();
    }

    public static void main(String[] args) {
        SpringApplication.run(LicenseApplication.class, args);
    }

}
