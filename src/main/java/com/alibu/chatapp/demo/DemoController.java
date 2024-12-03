package com.alibu.chatapp.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/messages")
public class DemoController {

    @GetMapping
    public ResponseEntity<List<String>> sayHello()
    {
        return ResponseEntity.ok(List.of("Hello", "World"));
    }
}
