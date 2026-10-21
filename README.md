# Warung Digital

Workspace containing three services:

- Customer Service: Spring Boot, port 8081
- Order Service: Spring Boot, port 8082
- Item Service: Quarkus, port 8083

## Local configuration

Local `application.properties` files are intentionally excluded from Git because
they may contain credentials. Copy each example file to `application.properties`
in the same directory, then provide the referenced password environment variable:

- Customer Service and Order Service: `SPRING_DATASOURCE_PASSWORD`
- Item Service: `QUARKUS_DATASOURCE_PASSWORD`

The services also expect their configured MySQL/PostgreSQL databases and Kafka
broker to be available locally.
