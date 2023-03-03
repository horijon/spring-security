# Getting Started

### Reference Documentation

### Spring Security Zero to Master along with JWT, OAUTH2

Udemy - https://www.udemy.com/course/spring-security-zero-to-master
Github - https://github.com/eazybytes/springsecurity6

MySql database - https://www.freemysqlhosting.net/
Database scripts - look inside resources/sql/scripts.sql
Reminder - the database might be down after some time, create another one and replace database params in the project

Filter usage
## implement Filter to create a filter and register it.
## Use GenericFilterBean if we need to use environment, filter and config related values.
## Use OncePerRequestFilter if we need to assure that the filter executes once every api request.

## Visit https://www.keycloak.org for keycloak
## Removed CustomAuthenticationProvider because we are going to leverage the keycloak Auth server.
## Removed filters related to JWT generators and validators.
## Removed passwordEncoder as well because we are going to leverage the keycloak Auth server.
## keycloak well known openid config = http://localhost:8180/realms/{realm_name}/.well-known/openid-configuration
### eg; http://localhost:8180/realms/springsecurity-dev/.well-known/openid-configuration

## For angular app and resource server app, we need to follow PKCE OAuth2 flow where keycloak acts as an Auth Server.
### First install keycloak-angular in the angular app -> npm install keycloak-angular keycloak-js
### Start keycloak -> Download keycloak zip file, then extract it and then inside its root folder execute in CLI the following:
### bin/kc.sh start-dev --http-port 8180

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.0.2/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.0.2/maven-plugin/reference/html/#build-image)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/3.0.2/reference/htmlsingle/#using.devtools)
* [Spring Web](https://docs.spring.io/spring-boot/docs/3.0.2/reference/htmlsingle/#web)

### Guides

The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)

