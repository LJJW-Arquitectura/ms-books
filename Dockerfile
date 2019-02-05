FROM java:8
FROM maven:alpine

WORKDIR /app

COPY . /app

RUN mvn -v
RUN mvn clean install -DskipTests

# Make port 8080 available to the world outside this container
EXPOSE 3002

ADD ./target/ms-books-0.0.1-SNAPSHOT.jar ms-books-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","ms-books-0.0.1-SNAPSHOT.jar"] 
