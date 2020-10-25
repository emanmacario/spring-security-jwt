package com.emanmacario.springsecurityjwt;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// This API is behind an authentication wall
@RestController
public class HelloResource {
    @RequestMapping("/hello")
    public String hello() {
        return "Hello World";
    }
}
