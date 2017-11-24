package com.lingaro.web;

import com.lingaro.web.dto.Person;
import com.lingaro.web.dto.PersonRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/person")
public class PersonController {

    private final PersonRepository personRepository;

    public PersonController(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @GetMapping()
    public List<Person> list() {
        return personRepository.findAll();
    }

    @PostMapping
    public Person add(@RequestBody Person person) {
        return personRepository.save(person);
    }

    @GetMapping("/add")
    public Person add(@RequestParam("name") String name) {
        Person person = new Person(name, "");
        return personRepository.save(person);
    }
}
