package com.yezi.secretgarden.controller;

import com.google.gson.Gson;
import com.sun.xml.bind.v2.schemagen.XmlSchemaGenerator;
import com.yezi.secretgarden.exception.InValidRegisterUserException;
import com.yezi.secretgarden.service.LoggerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.Method;
import java.net.URI;
import java.util.HashMap;

@org.springframework.web.bind.annotation.ControllerAdvice
@RequiredArgsConstructor
public class ControllerAdvice {
    private final LoggerService loggerService;

//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public ModelAndView handle(Exception e) {
//        loggerService.errorLoggerTest(e.getMessage());
//        return new ModelAndView("redirect:/", "msg", "검증 실패입니다. 관리자에게 문의하세요.");
//
//    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handle(BindException e) {
        BindingResult b = e.getBindingResult();

        loggerService.errorLoggerTest(b.getAllErrors().get(1).toString());
        loggerService.errorLoggerTest(e.getMessage());
        // redirect
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
