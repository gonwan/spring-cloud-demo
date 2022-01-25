package com.gonwan.springcloud.license.client;

import com.gonwan.springcloud.license.model.Organization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class OrganizationWebClient {

    private static final Logger logger = LoggerFactory.getLogger(OrganizationWebClient.class);

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Value("${application.organizationUrl}")
    private String organizationUrl;

    /*
     * Zuul adds correlationId, so go through it.
     * # curl -u eagleeye:thisissecret http://localhost:5555/api/auth/oauth/token -X POST -d "grant_type=password&scope=webclient&username=user&password=password1"
     * # curl -H "Authorization: Bearer 7559c856-4232-44c6-b85e-3b1a0f75bbe3" http://localhost:5555/api/license/v1/organizations/e254f8c-c442-4ebe-a82a-e2fc1d1ff78a/licenses/f3831f8c-c338-4ebe-a82a-e2fc1d1ff78a
     */
    @Cacheable(key = "#organizationId", value = "organizations")
    public Organization getOrganization(String organizationId) {
        logger.debug("Calling organization service with id: {}.", organizationId);
        Organization organization = webClientBuilder.build()
                .get()
                .uri(organizationUrl + "/v1/organizations/{organizationId}", organizationId)
                .retrieve()
                .bodyToMono(Organization.class)
                .block();
        return organization;
    }

    @CacheEvict(key = "#organizationId", value = "organizations")
    public void evictOrganization(String organizationId) {
        logger.debug("Evict organization id: {}", organizationId);
    }

}
