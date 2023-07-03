### Authentication Service (Next)

- The server utilize the new [spring authorization server](https://github.com/spring-projects/spring-authorization-server), which requires Java 17.
- The new implementation does not support `password` grant which is deprecated. The recommended alternative is `authroization_code` grant. But our servers are first-party application, this one may be overkilled. So other choices:
  - Implement the `password` grant ourselves.
  - Use `authorization_code` grant.
  - Use `client_credentials` grant, that has no dedicated user authorizations.
- Refer to the [official sample](https://github.com/spring-projects/spring-authorization-server/tree/main/samples) or [this one](https://github.com/Baeldung/spring-security-oauth/tree/master/oauth-authorization-server). The service is not working currently.
