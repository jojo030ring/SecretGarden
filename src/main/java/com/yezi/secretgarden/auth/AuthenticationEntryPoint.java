package com.yezi.secretgarden.auth;
// https://mighty96.github.io/til/access-authentication/

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nonapi.io.github.classgraph.json.JSONUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * 가입 되지 않은 사용자의 접근
 */
@Slf4j
@Component

public class AuthenticationEntryPoint implements org.springframework.security.web.AuthenticationEntryPoint {


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
//        String errorMessage ="";
//        if(exception instanceof BadCredentialsException) {
//            errorMessage = "아이디 또는 비밀번호가 맞지 않습니다. 다시 확인해주세요.";
//        } else if (exception instanceof InternalAuthenticationServiceException) {
//
//            errorMessage = "내부 시스템 문제로 로그인 요청을 처리할 수 없습니다. 관리자에게 문의하세요. ";
//        } else if (exception instanceof UsernameNotFoundException) {
//            errorMessage = "존재하지 않는 계정입니다. 회원가입 후 로그인해주세요.";
//        } else if (exception instanceof AuthenticationCredentialsNotFoundException) {
//            errorMessage = "인증 요청이 거부되었습니다. 관리자에게 문의하세요.";
//        } else if (exception instanceof InsufficientAuthenticationException){
//            errorMessage = "해당 페이지로 넘어가려면 인증을 받아야합니다.";
//        }else {
//            errorMessage = "알 수 없는 오류로 로그인 요청을 처리할 수 없습니다. 관리자에게 문의하세요.";
//        }
//        request.setAttribute("msg", errorMessage);
//        request.setAttribute("error",exception);
//        response.sendRedirect("/secretgarden/login");
//        System.out.println(1111);
//        response.setStatus(HttpStatus.FORBIDDEN.value());
//        JsonObject jsonObject = new JsonObject();
//        jsonObject.addProperty("msg", "인증 요청이 거부되었습니다.");
//        String json = jsonObject.getAsString();
//        response.setCharacterEncoding("UTF-8");
//        PrintWriter printWriter = response.getWriter();
//        printWriter.write(json);
//        printWriter.flush();
//        response.setStatus(HttpStatus.FORBIDDEN.value());
//        System.out.println("sadfasdfsdaf");
        response.sendRedirect("/secretgarden/login");

    }


}
