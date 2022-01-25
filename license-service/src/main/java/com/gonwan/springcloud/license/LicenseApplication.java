package com.gonwan.springcloud.license;

import com.gonwan.springcloud.license.controller.LicenseServiceController;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.server.resource.web.reactive.function.client.ServletBearerExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
@EnableEurekaClient
@EnableCaching
public class LicenseApplication {

    @Bean
    public GroupedOpenApi groupedOpenApi() {
        return GroupedOpenApi.builder()
                .group("License API")
                .packagesToScan(LicenseServiceController.class.getPackage().getName())
                .build();
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Spring Cloud Demo API")
                        .termsOfService("http://www.gonwan.com/")
                        .version("2.0"));
    }

    @Bean
    public OAuth2AuthorizedClientManager oauth2AuthorizedClientManager(
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientRepository authorizedClientRepository) {
        OAuth2AuthorizedClientProvider oauth2AuthorizedClientProvider = OAuth2AuthorizedClientProviderBuilder.builder()
                .authorizationCode()
                .clientCredentials()
                .refreshToken()
                .password()
                .build();
        DefaultOAuth2AuthorizedClientManager oauth2AuthorizedClientManager =
                new DefaultOAuth2AuthorizedClientManager(clientRegistrationRepository, authorizedClientRepository);
        oauth2AuthorizedClientManager.setAuthorizedClientProvider(oauth2AuthorizedClientProvider);
        return oauth2AuthorizedClientManager;
    }

    private WebClient.Builder webClientBuilder(OAuth2AuthorizedClientManager oauth2AuthorizedClientManager) {
        ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2Client =
                new ServletOAuth2AuthorizedClientExchangeFilterFunction(oauth2AuthorizedClientManager);
        return WebClient.builder()
                .filter(new ServletBearerExchangeFilterFunction()) /* bearer token propagation */
                .apply(oauth2Client.oauth2Configuration()); /* oauth2 client support, no use currently. */
    }

    @LoadBalanced
    @Bean
    @ConditionalOnProperty(prefix = "eureka", name = "client.enabled", havingValue = "true", matchIfMissing = true)
    public WebClient.Builder lbOauth2WebClientBuilder(OAuth2AuthorizedClientManager oauth2AuthorizedClientManager) {
        return webClientBuilder(oauth2AuthorizedClientManager);
    }

    @Bean
    @ConditionalOnProperty(prefix = "eureka", name = "client.enabled", havingValue = "false", matchIfMissing = false)
    public WebClient.Builder oauth2WebClientBuilder(OAuth2AuthorizedClientManager oauth2AuthorizedClientManager) {
        return webClientBuilder(oauth2AuthorizedClientManager);
    }

    /*
     * Required for spring boot 2.0, or default JDK serializers are used.
     * See: RedisCacheConfiguration#cacheManager().
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration
                .defaultCacheConfig()
                .computePrefixWith(name -> name + ":")  /* override the default "::" */
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()));
        return RedisCacheManager.builder(connectionFactory).cacheDefaults(cacheConfiguration).build();
    }

    public static void main(String[] args) {
        SpringApplication.run(LicenseApplication.class, args);
    }

}
