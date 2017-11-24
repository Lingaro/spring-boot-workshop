package com.lingaro.web.person;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.lingaro.web.person.PersonServiceSpringTest.assertLimitReached;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PersonServiceTest {
    @Mock
    PersonRepository personRepository;
    @InjectMocks
    PersonService personService;

    @Test
    public void save() throws Exception {
        when(personRepository.count()).thenReturn(5L);
        assertLimitReached(personService);
    }

}