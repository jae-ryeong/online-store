package com.project.onlinestore;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class TestController {

    @GetMapping("/api/movie")
    public List<String> MovieList(){
        return Arrays.asList("영화보기", "Movie");
    }

    @GetMapping("/demo/api/data")
    public String demo() {
        return "Hello React";
    }

}
