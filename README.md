# Introduction
The demo project is initialized from [https://github.com/carnellj/spmia-chapter10](https://github.com/carnellj/spmia-chapter10). Additions are:
- Code cleanup, bug fix, and better comments.
- Java 9+ support.
- Spring Boot 2.0 migration.
- Switch from Postgres to MySQL, and from Kafka to RabbitMQ.
- Easier local debugging by switching off service discovery and remote config file lookup.
- Kubernetes support.
- Swagger Integration.
- Spring Boot Admin Integration.

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
- [kubernetes](kubernetes): Kubernetes support.

**NOTE**: The new OAuth2 support in Spring is actively being developed. All functions are merging into core Spring Security 5. As a result, current implementation is suppose to change. See:
- [https://spring.io/blog/2018/01/30/next-generation-oauth-2-0-support-with-spring-security](https://spring.io/blog/2018/01/30/next-generation-oauth-2-0-support-with-spring-security)
- [https://github.com/spring-projects/spring-security/wiki/OAuth-2.0-Features-Matrix](https://github.com/spring-projects/spring-security/wiki/OAuth-2.0-Features-Matrix)

# Tested Dependencies
- Java 8+
- Docker 1.13+
- Kubernetes 1.11+

# Building Docker Images
```
export BUILD_NAME=2.0.0
mvn clean package docker:build
```
In case of running out of disk space, clean up unused images and volumes with:
```
docker rmi $(docker images -f "dangling=true" -q)
docker volume prune
```

# Running Docker Compose
```
export BUILD_NAME=2.0.0
docker-compose -f docker/docker-compose.yml up
```
Or with separate services:
```
docker-compose -f docker/docker-compose.yml up authentication-service organization-service license-service
```

# Running Kubernetes
NOTE: Kubernetes does not support environment variable substitution by default.
```
kubectl create -f kubernetes/kubernetes.yml
```

# Use Cases
Suppose you are using the kubernetes deployment.

### Get OAuth2 token
`curl` is used here, and 31004 is the cluster-wide port of the Zuul gateway server:
```
# curl -u eagleeye:thisissecret http://172.16.87.12:31004/api/auth/oauth/token -X POST -d "grant_type=password&scope=webclient&username=user&password=password1"
{"access_token":"d3b817dc-fb7a-4e65-a080-d0e34c0dc4d5","token_type":"bearer","refresh_token":"a5d12d05-78ff-4170-ab4f-b9c4e9886358","expires_in":41496,"scope":"webclient"}
```
### Get organization info
Use the token returned from previous request.
```
# curl -H "Authorization: Bearer d3b817dc-fb7a-4e65-a080-d0e34c0dc4d5" http://172.16.87.12:31004/api/organization/v1/organizations/e254f8c-c442-4ebe-a82a-e2fc1d1ff78a
{"id":"e254f8c-c442-4ebe-a82a-e2fc1d1ff78a","name":"customer-crm-co","contactName":"Mark Balster","contactEmail":"mark.balster@custcrmco.com","contactPhone":"823-555-1212"}
```
### Get license info associated with organization info
Use the token returned from previous request.
```
# curl -H "Authorization: Bearer d3b817dc-fb7a-4e65-a080-d0e34c0dc4d5" http://172.16.87.12:31004/api/license/v1/organizations/e254f8c-c442-4ebe-a82a-e2fc1d1ff78a/licenses/f3831f8c-c338-4ebe-a82a-e2fc1d1ff78a
{"id":"f3831f8c-c338-4ebe-a82a-e2fc1d1ff78a","organizationId":"e254f8c-c442-4ebe-a82a-e2fc1d1ff78a","organizationName":"customer-crm-co","contactName":"Mark Balster","contactPhone":"823-555-1212","contactEmail":"mark.balster@custcrmco.com","productName":"CustomerPro","licenseType":"user","licenseMax":100,"licenseAllocated":5,"comment":null}
```

### Distributed Tracing via Zipkin
Every response contains a correlation ID to help diagnose possible failures among service call. Run with `curl -v` to get it:
```
# curl -v ...
...
< sc-correlation-id: 3265b50156556c05
...
```
Search it in Zipkin to get all trace info, including latencies if you are interested in.
![zipkin-1](https://raw.githubusercontent.com/gonwan/spring-cloud-demo/master/images/zipkin-1.png)
![zipkin-2](https://raw.githubusercontent.com/gonwan/spring-cloud-demo/master/images/zipkin-2.png)

The license service caches organization info in Redis, prefixed with `organizations:`. So you may want to clear them to get a complete tracing of cross service invoke.
```
redis-cli -h 172.16.87.12 -c del $(redis-cli -h 172.16.87.12 -c keys organizations* | gawk '{ print $1 }')
```

### Working with OAuth2
All OAuth2 tokens are cached in Redis, prefixed with `oauth2:`. There is also JWT token support. Comment/Uncomment `@Configuration` in `AuthorizationServerConfiguration` and `JwtAuthorizationServerConfiguration` classes to switch it on/off.

### Swagger Integration
The organization service and license service have Swagger integration. Access via `/swagger-ui.html`.

### Spring Boot Admin Integration
Spring Boot Admin is integrated into the eureka server. Access via: `http://${eureka-server}:8761/admin`.
![sba-1](https://raw.githubusercontent.com/gonwan/spring-cloud-demo/master/images/sba-1.png)
