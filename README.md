# Agenda

Prerequisites:

* JDK 8
* IDE (prefered: Intellij Idea Comunity or Ultimate)
* git
* sdkman

We'll create a Spring Boot project from scratch.

* json endpoints
* embeded H2 database
* data repository
* dependency injection
* logging
* testing
* serving static files
* packaging (war, jar)
* profiles
* security
* deployment as linux service

# Spring CLI

[Install sdkman](https://docs.spring.io/spring-boot/docs/current/reference/html/getting-started-installing-spring-boot.html#getting-started-sdkman-cli-installation)

    sdk install springboot
    
    spring init my-project

## Empty project walkthrough
* pom.xml 
* maven wrapper
* main class
* properties
* test

Just starts and exits - it's not a web server yet.  
Spring Boot apps also can be:

* CLI
* batch
* GUI

## git

    git init
Note: there's already a `.gitignore'

## Rename
* pom: artefect & group
* DemoApplication
* DemoApplicationTests
* package

## Web
* `pom.xml`: `spring-boot-starter-web`
(enable idea maven auto import)
```
@RestController
@SpringBootApplication
public class Application {

  @GetMapping("/")
  public String hello() {
    return "hello";
  }

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
```

## [Postman](https://www.getpostman.com/)

REST client.

## Profiles

```
java -jar target/spring-project.jar --spring.profiles.active=dev
```
# Deploing

* `war` on App Server
* Linux service
* Docker container

## Linux service
```
mvn clean package -DskipTests

sudo cp ./spring.service /etc/systemd/system/ -i 
sudo useradd app
sudo mkdir -p /var/spring/
sudo cp target/spring-project.jar /var/spring/
sudo chown app:app -R /var/spring

sudo systemctl daemon-reload 
sudo systemctl start spring
sudo systemctl status spring

sudo systemctl enable spring # start at boot
```
View logs:
```
journalctl -u spring  
journalctl -fu spring # follow
```

## Docker container
Build the image:
```
docker build -t spring-app .
```
Start the service:
```
docker service create -p 80:8080 --name spring-service spring-app
```
Optionally test (just run) the container:
```
docker run -it --rm -p 8080:8080 spring-app
```