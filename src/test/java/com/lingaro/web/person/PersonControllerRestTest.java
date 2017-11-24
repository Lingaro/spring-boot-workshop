package com.lingaro.web.person;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class PersonControllerRestTest {
    @Autowired
    TestRestTemplate rest;

    @Before
    public void authenticate() {
        rest = rest.withBasicAuth("admin", "pass");
    }

    @Test
    public void person() throws Exception {
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