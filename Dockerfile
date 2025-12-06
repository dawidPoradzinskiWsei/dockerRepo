FROM eclipse-temurin:21 AS build

WORKDIR /app

copy pom.xml .
copy initial/src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21

WORKDIR /app

COPY --from=build /app/target/rest-service-initial-0.0.1-SNAPSHOT.jar  /app/demo.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/demo.jar"]