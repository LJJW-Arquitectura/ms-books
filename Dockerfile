FROM java:8
FROM maven:alpine

WORKDIR /app

COPY . /app

RUN mvn -v
RUN mvn clean install -DskipTests
EXPOSE 8080
ADD ./target/ms-books-0.0.1-SNAPSHOT.jar ms-books-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","ms-books-0.0.1-SNAPSHOT.jar"]