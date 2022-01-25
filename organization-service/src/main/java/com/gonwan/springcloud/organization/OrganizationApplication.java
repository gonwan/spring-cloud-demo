package com.gonwan.springcloud.organization;

import com.gonwan.springcloud.organization.controller.OrganizationServiceController;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableEurekaClient
@EnableBinding(Source.class)
public class OrganizationApplication {

    @Bean
    public GroupedOpenApi groupedOpenApi() {
        return GroupedOpenApi.builder()
                .group("Organization API")
                .packagesToScan(OrganizationServiceController.class.getPackage().getName())
                .build();
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Spring Cloud Demo API")
                        .termsOfService("https://www.gonwan.com/")
                        .version("2.0"))
                .components(new Components()
                        .addSecuritySchemes("OAuth2-Token",
                                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("uuid")));

    }

    public static void main(String[] args) {
        SpringApplication.run(OrganizationApplication.class, args);
    }

}
