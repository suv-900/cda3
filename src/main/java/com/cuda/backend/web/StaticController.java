package com.cuda.backend.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StaticController {
   
    @GetMapping(path = "/ping")
    public String pong(){
        return "pong.html";
    }

    @GetMapping("/home")
    public String serveHomePage(){
        return "home.html";
    }
}
