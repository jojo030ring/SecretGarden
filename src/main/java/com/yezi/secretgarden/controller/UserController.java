package com.yezi.secretgarden.controller;

import com.google.gson.Gson;
import com.yezi.secretgarden.domain.User;
import com.yezi.secretgarden.domain.UserRegisterRequest;
import com.yezi.secretgarden.exception.InValidRegisterUserException;
import com.yezi.secretgarden.repository.UserRepository;
import com.yezi.secretgarden.service.LoggerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/secretgarden")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final LoggerService loggerService;
    @GetMapping("/register")
    public String register() {
        return "register.html";
    }

    @PostMapping("/register")
    public String register( @Valid UserRegisterRequest uRRequest,BindingResult bindingResult, Model m) {
            if(!validate(bindingResult)) {

                m.addAttribute("result", false);
                throw new InValidRegisterUserException();
//                return "redirect:/secretgarden/register";

            }else {
                userRepository.save(uRRequest);
                return "redirect:/";

            }
        }

    public boolean validate(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<ObjectError> errors = bindingResult.getAllErrors();

            for (ObjectError e : errors) {
                loggerService.errorLoggerTest("validation failed :" + e.toString());
            }
            return false;
        }
        return true;
    }

}




