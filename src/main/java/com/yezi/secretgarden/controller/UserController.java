package com.yezi.secretgarden.controller;

import com.google.gson.Gson;
import com.yezi.secretgarden.domain.User;
import com.yezi.secretgarden.domain.UserRegisterRequest;
import com.yezi.secretgarden.exception.InValidRegisterUserException;
import com.yezi.secretgarden.service.LoggerService;
import com.yezi.secretgarden.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/secretgarden")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final LoggerService loggerService;
    @GetMapping("/register")
    public String register() {
        return "register";
    }


//    @ResponseBody
//    public String register( @Valid @RequestBody  UserRegisterRequest uRRequest,BindingResult bindingResult, Model m) {
//
//        Gson gson = new Gson();
//        HashMap<String, String> map = new HashMap<>();
//            if(!validate(bindingResult)) {
//                m.addAttribute("result", false);
//                throw new InValidRegisterUserException();
//               //map.put("result","false");
//                return gson.toJson(map);
//
//            }else {
//                System.out.println(uRRequest);
//                userService.registerUser(uRRequest);
//                map.put("result","true");
//                return gson.toJson(map);
//
//            }
//        }
    @PostMapping(
        value = "/register"
        , headers="Accept=application/json")
    @ResponseBody
    public String register(@Valid @RequestBody UserRegisterRequest uRRequest, Model m) {
        Gson gson = new Gson();
        userService.registerUser(uRRequest);
        return gson.toJson(new HashMap<>().put("result","false"));

    }

    @GetMapping("/login")
    public String login() {
        return "login";
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




