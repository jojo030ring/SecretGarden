package com.yezi.secretgarden.controller;

import com.yezi.secretgarden.domain.request.UserRegisterRequest;
import com.yezi.secretgarden.jwt.JwtTokenUtil;
import com.yezi.secretgarden.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;

@Controller
@RequestMapping("${header.url.secretgarden}")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;
    @Value("${header.url.secretgarden}")
    private String HOME_PATH;
    @GetMapping("/register")
    public String register() {
        return "register";
    }


    @PostMapping(
        value = "/register"
        , headers="Accept=application/json")
    @ResponseBody
    // test를 위해 @Valid 빼놓음
    public ResponseEntity<HashMap<String,String>> register( @RequestBody  UserRegisterRequest uRRequest) { // @Valid 테스트를 위해서 빼놓음
        userService.registerUser(uRRequest);
        HashMap<String,String> map = new HashMap<>();
        map.put("msg","회원 가입을 완료했습니다.");
        map.put("url",HOME_PATH+"/login");
        return new ResponseEntity<HashMap<String, String>>(map,HttpStatus.OK);

    }

    @GetMapping("/login")
    public String loginForm(HttpServletRequest request) {
        Cookie token = WebUtils.getCookie(request,"token");
        if(token != null && jwtTokenUtil.validateToken(jwtTokenUtil.getPureJWT(jwtTokenUtil.decodeFromCookieToJWT(request)))) { // 쿠키가 있다..?
            return "redirect:"+HOME_PATH+"/";
        }

        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response, Model m) {
        Cookie cookie = WebUtils.getCookie(request,"token");
        if(cookie != null) {
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
        return "redirect:"+HOME_PATH+"/login";
    }









}




