package com.yezi.secretgarden.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/secretgarden")
public class HomeController {
    @GetMapping("/")
    public String home() {
        return "index";
    }
}
