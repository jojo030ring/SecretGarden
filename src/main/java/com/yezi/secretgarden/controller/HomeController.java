package com.yezi.secretgarden.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/secretgarden")
public class HomeController {
    @GetMapping("/")
    public String home(HttpServletRequest request, HttpServletResponse response) {
        return "redirect:/secretgarden/board";
    }
}
