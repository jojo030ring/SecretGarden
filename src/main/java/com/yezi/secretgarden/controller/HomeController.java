package com.yezi.secretgarden.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("${header.url.secretgarden}")
public class HomeController {
    @Value("${header.url.secretgarden}")
    private String HOME_PATH;
    @GetMapping("/")
    public String home(HttpServletRequest request, HttpServletResponse response) {
        return "redirect:"+HOME_PATH+"/board";
    }
}
