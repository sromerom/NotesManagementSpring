package com.liceu.sromerom.controllers;


import com.liceu.sromerom.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @Autowired
    UserService userService;



    @GetMapping("/")
    public String index() {
        return "index";
    }


    @GetMapping("/restrictedArea")
    public String error() {
        return "error";
    }
}
