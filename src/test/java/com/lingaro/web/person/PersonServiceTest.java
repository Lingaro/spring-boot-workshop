package com.lingaro.web.person;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static com.lingaro.web.person.PersonServiceSpringTest.assertLimitReached;
import static org.mockito.Mockito.when;

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