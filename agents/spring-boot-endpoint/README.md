# MoSKito Control Agent — Spring Boot Starter

Exposes the `moskito-control-agent` data as a **Spring Boot Actuator endpoint** at
`/actuator/moskitocontrol`, so a Spring Boot application can be monitored by MoSKito Control
without registering the servlet filter (`moskito-control-agent-http-endpoint`).

It is the Spring-idiomatic counterpart of the HTTP filter endpoint: same data, but it lives on
the management port, is secured like any other actuator endpoint, and is enabled the way a Spring
developer expects. The **wire protocol is identical** to the http-endpoint, so MoSKito Control
reads it with the existing `HttpConnector` — only the path differs.

## Usage

1. Add the dependency (you already have `spring-boot-starter-actuator` and moskito-core in the app):

```xml
<dependency>
    <groupId>org.moskito</groupId>
    <artifactId>moskito-control-agent-spring-boot-starter</artifactId>
    <version>${moskito-control.version}</version>
</dependency>
```

2. Expose the endpoint (it is disabled by default, like all non-default actuator endpoints):

```properties
management.endpoints.web.exposure.include=health,moskitocontrol
```

That's it — the endpoint is auto-configured. Verify:

```
GET /actuator/moskitocontrol            -> help
GET /actuator/moskitocontrol/status     -> threshold status + now-running
GET /actuator/moskitocontrol/thresholds
GET /actuator/moskitocontrol/accumulators
GET /actuator/moskitocontrol/accumulator/{name}[/{name}...]
GET /actuator/moskitocontrol/info
GET /actuator/moskitocontrol/config
GET /actuator/moskitocontrol/nowrunning
```

## MoSKito Control side

Configure the component with a `HTTP` connector and point `agentPath` at the actuator path
(`location` stays the application base URL):

```json
{
  "name": "my-spring-service",
  "connectorType": "HTTP",
  "location": "https://my-service:8080",
  "agentPath": "/actuator/moskitocontrol/"
}
```

`agentPath` defaults to `/moskito-control-agent/` (the servlet filter mapping), so existing
components that use the http-endpoint keep working unchanged.

## Notes

- Requires **Spring Boot 3 / Java 17+**. For that reason this module is only built by the reactor
  when compiling on JDK 17 or newer (see the `spring-boot-endpoint` profile in `../pom.xml`).
- The reply envelope (`protocolVersion` / `timestamp` / `reply`) and payload beans are shared with
  the http-endpoint via the `moskito-control-agent` module. `WireCompatibilityTest` locks the
  contract by serializing with Jackson (as Spring Boot does) and re-parsing with the actual
  control-side parser (which uses gson), guarding against serializer drift.
