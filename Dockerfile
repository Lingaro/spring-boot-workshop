FROM openjdk:8-jdk-alpine
ADD target/spring-project.jar .
ENTRYPOINT ["java","-jar","spring-project.jar"]
