package spring.practice.oct25.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@RequestMapping
@RestController
public class MyRestController {

    @GetMapping("/hello")
    public String sayHello(){
        return  "hello sir";
    }
}
