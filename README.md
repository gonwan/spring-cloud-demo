# Introduction
The demo project is initialized from [https://github.com/carnellj/spmia-chapter10](https://github.com/carnellj/spmia-chapter10). Additions are:
- Code cleanup, bug fix, and better comments.
- Java 9+ support.
- Spring Boot 2.0 migration.
- Switch from Postgres to MySQL, and from Kafka to RabbitMQ.
- Easier local debugging by swithing off service discovery and remote config file lookup.
- (TODO) Kubernetes support.

The project includes:
- [eureka-server](eureka-server): Service for service discovery. Registered services are shown on its web frontend, running at 8761 port.
- [config-server](config-server): Service for config file management. Config files can be accessed via: `http://${config-server}:8888/${appname}/${profile}`. Where `${appname}` is `spring.application.name` and `${profile}` is something like `dev`, `prd` or `default`.
- [zipkin-server](zipkin-server): Service to aggregate distributed tracing data, working with [spring-cloud-sleuth](https://github.com/spring-cloud/spring-cloud-sleuth/). It runs at 9411 port. All cross service requests, message bus delivery are traced by default.
- [zuul-server](zuul-server): Gateway service to route requests, running at 5555 port.
- [authentication-service](authentication-service): OAuth2 enabled authentication service running at 8901. Redis is used for token cache. JWT support is also included. Spring Cloud Security 2.0 saves a lot when building this kind of services. 
- [organization-service](organization-service): Application service holding organization information, running at 8085. It also acts as an OAuth2 client to `authentication-service` for authorization.
- [license-service](license-service): Application service holding license information, running at 8080. It also acts as an OAuth2 client to `authentication-service` for authorization.
- [config](config): Config files hosted to be accessed by `config-server`.
- [docker](docker): Docker compose support.
- (TODO) [kubernetes](kubernetes): Kubernetes support.

**NOTE**: The new OAuth2 support in Spring are actively developing. All functions are merging into core Spring Security 5. As a result, current implementation is suppose to change. See:
- [https://spring.io/blog/2018/01/30/next-generation-oauth-2-0-support-with-spring-security](https://spring.io/blog/2018/01/30/next-generation-oauth-2-0-support-with-spring-security)
- [https://github.com/spring-projects/spring-security/wiki/OAuth-2.0-Features-Matrix](https://github.com/spring-projects/spring-security/wiki/OAuth-2.0-Features-Matrix)

# Tested Dependencies
- Java 8+
- Docker 1.13+
- Kubernetes ???

# Building Docker Images
```
export BUILD_NAME=1.0.0
mvn clean package docker:build
```
In case of running out of disk space, clean up unused images and volumes with:
```
docker rmi $(docker images -f "dangling=true" -q)
docker volume prune
```

# Running Docker Compose
```
export BUILD_NAME=1.0.0
docker-compose -f docker/docker-compose.yml up
```
Or with separate services:
```
docker-compose -f docker/docker-compose.yml up authentication-service organization-service license-service
```

# Running Kubernetes
TODO...
