package com.lingaro.web.person;

import org.springframework.stereotype.Service;

import javax.annotation.security.RolesAllowed;
import java.util.logging.Logger;

@Service
public class PersonService {
    public static final Logger LOG = Logger.getLogger(PersonService.class.getName());

    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
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
