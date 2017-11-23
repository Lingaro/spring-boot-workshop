package com.lingaro.web;

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
}
