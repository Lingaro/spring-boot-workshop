##### com/lingaro/web/person/PersonController.java
```java
@RestController
@RequestMapping("/person")
class PersonController {
  private final PersonRepository personRepository;
  private final PersonService personService;
  PersonController(PersonRepository personRepository, PersonService personService) {
    this.personRepository = personRepository;
    this.personService = personService;
  }
  @GetMapping()
  public List<Person> list() {
    return personRepository.findAll();
  }
  @PostMapping
  public Person add(@RequestBody Person person) {
    return personService.save(person);
  }
}
```
##### com/lingaro/web/person/PersonRepository.java
```java
@Repository
interface PersonRepository extends JpaRepository<Person, Integer> {
}
```
##### com/lingaro/web/person/Person.java
```java
@Entity
public class Person {
  @Id
  @GeneratedValue
  public int id;
  @NotEmpty
  public String name;
  @NotEmpty
  public String surname;
  Person() {
  }
  public Person(String name, String surname) {
    this.name = name;
    this.surname = surname;
  }
  @Override
  public String toString() {
    return "Person{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", surname='" + surname + '\'' +
        '}';
  }
}
```
##### com/lingaro/web/person/PersonService.java
```java
@Service
class PersonService {
  public static final Logger LOG = Logger.getLogger(PersonService.class.getName());
  private final PersonRepository personRepository;
  PersonService(PersonRepository personRepository) {
    this.personRepository = personRepository;
  }
  @RolesAllowed("ADMIN")
  public Person save(Person person) {
    LOG.fine("New person: " + person);
    if(personRepository.count() >= 5) {
      throw new IllegalArgumentException("Person limit reached");
    }
    return personRepository.save(person);
  }
}
```
##### com/lingaro/web/SecurityConfig.java
```java
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(jsr250Enabled = true)
class SecurityConfig extends WebSecurityConfigurerAdapter {
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .authorizeRequests()
        .antMatchers("/person").authenticated()
        .and()
        .httpBasic()
        .and().csrf().disable();
  }
  @Autowired
  void configureGlobal(AuthenticationManagerBuilder auth, PasswordEncoder encoder) throws Exception {
    auth
        .inMemoryAuthentication()
        .withUser("user").password(encoder.encode("pass")).roles("USER").and()
        .withUser("admin").password(encoder.encode("pass")).roles("ADMIN");
  }
  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
```
##### com/lingaro/web/Application.java
```java
@SpringBootApplication
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
```
##### com/lingaro/web/security/DevAuth.java
```java
@Configuration
@Profile("dev")
class DevAuth {
  @Autowired
  public void configureDev(AuthenticationManagerBuilder auth, PasswordEncoder encoder) throws Exception {
    auth
        .inMemoryAuthentication()
        .withUser("test").password(encoder.encode("test")).roles("ADMIN");
  }
}
```
##### src/main/resources/static/index.html
```html
<h1>Hello HTML</h1>
```
##### src/main/resources/application.properties
```properties
spring.h2.console.enabled=false
spring.datasource.url=jdbc:h2:file:./target/db;DB_CLOSE_ON_EXIT=FALSE
```
##### src/main/resources/application-dev.properties
```properties
spring.h2.console.enabled=true
spring.datasource.url=jdbc:h2:file:./target/db;DB_CLOSE_ON_EXIT=FALSE
logging.level.com.lingaro=DEBUG
```
##### src/test/java/com/lingaro/web/person/PersonControllerRestTest.java
```java
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("dev")
public class PersonControllerRestTest {
  @Autowired
  TestRestTemplate rest;
  @Before
  public void authenticate() {
    rest = rest.withBasicAuth("test", "test");
  }
  @Test
  public void person() {
    List<Person> initial = rest.getForEntity("/person", List.class).getBody();
    assertThat(
        initial,
        Matchers.emptyCollectionOf(Person.class)
    );
    Person result = rest.postForObject("/person", new Person("Krzysztof", "Jeżyna"), Person.class);
    assertThat(result.id, Matchers.greaterThan(0));
    assertEquals("Krzysztof", result.name);
    assertEquals("Jeżyna", result.surname);
    Person[] list = rest.getForEntity("/person", Person[].class).getBody();
    assertThat(
        Arrays.asList(list),
        Matchers.hasSize(1)
    );
    assertThat(list[0], Matchers.samePropertyValuesAs(result));
  }
}
```
##### src/test/java/com/lingaro/web/person/PersonServiceTest.java
```java
@RunWith(MockitoJUnitRunner.class)
public class PersonServiceTest {
  @Mock
  PersonRepository personRepository;
  @InjectMocks
  PersonService personService;
  @Test
  public void save() {
    when(personRepository.count()).thenReturn(5L);
    assertLimitReached(personService);
  }
}
```
##### src/test/java/com/lingaro/web/person/PersonServiceSpringTest.java
```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class PersonServiceSpringTest {
  @Autowired
  PersonRepository personRepository;
  @Autowired
  PersonService personService;
  @Test
  @WithMockUser(roles="ADMIN")
  public void save() {
    personRepository.deleteAllInBatch();
    for(int i=0;i<5;i++){
      personService.save(new Person("Jon","Snow"));
    }
    assertLimitReached(personService);
  }
  public static void assertLimitReached(PersonService personService) {
    try{
      personService.save(new Person("Rick","Sanchez"));
      fail("Exception expected");
    }catch (IllegalArgumentException ex){
      assertThat(ex.getMessage(), Matchers.containsString("limit reached"));
    }
  }
}
```
##### src/test/java/com/lingaro/web/ApplicationTests.java
```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {
	@Test
	public void contextLoads() {
	}
}
```
##### pom.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<groupId>com.lingaro.web</groupId>
	<artifactId>spring-project</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<packaging>jar</packaging>
	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>2.1.1.RELEASE</version>
		<relativePath/> <!-- lookup parent from repository -->
	</parent>
	<properties>
		<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
		<project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
		<java.version>1.8</java.version>
	</properties>
	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-security</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.security</groupId>
			<artifactId>spring-security-test</artifactId>
		</dependency>
		<dependency>
			<groupId>com.h2database</groupId>
			<artifactId>h2</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
	</dependencies>
	<build>
    <finalName>${project.artifactId}</finalName>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
				<configuration>
					<executable>true</executable>
				</configuration>
			</plugin>
		</plugins>
	</build>
</project>
```
##### requests.http
```http
POST http://admin:pass@localhost:8080/person
Content-Type: application/json
{
  "name": "Rick",
  "surname": "Sanchez"
}
###
POST http://admin:pass@localhost:8080/person
Content-Type: application/json
{
  "name": "Peter",
  "surname": "Griffin"
}
###
GET http://user:pass@localhost:8080/person
### dev only:
GET http://test:test@localhost:8080/person
###
POST http://test:test@localhost:8080/person
Content-Type: application/json
{
  "name": "Dev",
  "surname": "User"
}
###
```
##### spring.service
```service
#
# systemd service descriptor
# Place this file in /etc/systemd/system/spring.service
# Control the service using systemctl e.g.:
#   sudo systemctl restart spring
#
[Unit]
Description=spring
After=syslog.target
[Service]
User=app
ExecStart=/var/spring/spring-project.jar
SuccessExitStatus=143
[Install]
WantedBy=multi-user.target
```
##### Dockerfile
```Dockerfile
FROM openjdk:8-jdk-alpine
ADD target/spring-project.jar .
ENTRYPOINT ["java","-jar","spring-project.jar"]
```
