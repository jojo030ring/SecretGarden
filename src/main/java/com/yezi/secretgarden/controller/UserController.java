package com.yezi.secretgarden.controller;

import com.yezi.secretgarden.domain.request.UserRegisterRequest;
import com.yezi.secretgarden.service.LoggerService;
import com.yezi.secretgarden.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

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


    @PostMapping(
        value = "/register"
        , headers="Accept=application/json")
    @ResponseBody
    public ResponseEntity<HashMap<String,String>> register( @RequestBody UserRegisterRequest uRRequest) { // @Valid 테스트를 위해서 빼놓음
        userService.registerUser(uRRequest);
        HashMap<String,String> map = new HashMap<>();
        map.put("msg","회원 가입을 완료했습니다.");
        map.put("url","/secretgarden/login");
        return new ResponseEntity<HashMap<String, String>>(map,HttpStatus.OK);

    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response, Model m) {
        Cookie cookie = WebUtils.getCookie(request,"token");
        if(cookie != null) {
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
        return "redirect:/secretgarden/login";
    }









}




