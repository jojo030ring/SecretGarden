package com.yezi.secretgarden.controller;

import com.google.gson.Gson;
import com.yezi.secretgarden.domain.User;
import com.yezi.secretgarden.domain.UserRegisterRequest;
import com.yezi.secretgarden.exception.InValidRegisterUserException;
import com.yezi.secretgarden.service.LoggerService;
import com.yezi.secretgarden.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @PostMapping("/login")
    public String login() {
        return "redirect:/";
    }
    @Secured("ROLE_ADMIN")
    @GetMapping("/info")
    public @ResponseBody String info() {
        return "개인정보";
    }
    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_USER')") // data 함수가 시작되기 전에 지정됨
    @GetMapping("/data")
    public @ResponseBody String data() {
        return "데이터 정보";
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




