package com.yezi.secretgarden.controller;

import com.yezi.secretgarden.domain.User;
import com.yezi.secretgarden.domain.UserRegisterRequest;
import com.yezi.secretgarden.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/secretgarden")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    @GetMapping("/register")
    public String register() {
        return "register.html";
    }

    @PostMapping("/register")
    public String register(UserRegisterRequest uRRequest, Model m) {

        userRepository.save(uRRequest);
        System.out.println(uRRequest);
        return "redirect:/";

    }

}
