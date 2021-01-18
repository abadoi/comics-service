FROM maven:3.5-jdk-8 AS build
COPY src /usr/src/app/src  
COPY pom.xml /usr/src/app  
RUN mvn -f /usr/src/app/pom.xml clean package

FROM openjdk:8-alpine
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
COPY --from=build /usr/src/app/target/*.jar /usr/app/app.jar
ENTRYPOINT ["java","-jar","/usr/app/app.jar"]

EXPOSE 8080
