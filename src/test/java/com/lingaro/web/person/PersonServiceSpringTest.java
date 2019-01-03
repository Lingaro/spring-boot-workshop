package com.lingaro.web.person;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

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