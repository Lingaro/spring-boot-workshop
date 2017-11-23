package com.lingaro.web;

import com.lingaro.web.dto.Person;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class WelcomeController {
    @GetMapping
    public String world() {
        return "world";
    }

    @GetMapping("mars")
    public String mars() {
        return "mars";
    }

    @GetMapping("/person")
    public Person person() {
        return new Person("Krzysztof", "Jeżyna");
    }
}
