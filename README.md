# MS-BOOKS
This repository contains a Spring Boot Java project designed to run as a dockerized microservice that presents a REST API.

To run this project, deploy it on a suitable Docker Container

```
mvn clean install -DskipTests
```

```
docker-compose build
```

```
docker-compose up
```

The microservice has an internal structure divided in the following way:

Controller: REST API.
Entity: Data model.
Repository: Data access object.
Service: Business Logic.
Response: Auxiliary classes for API responses.
Exception: Management of exceptions.