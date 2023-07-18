package com.yezi.secretgarden.controller;

import com.sun.xml.bind.v2.schemagen.XmlSchemaGenerator;
import com.yezi.secretgarden.exception.InValidRegisterUserException;
import com.yezi.secretgarden.service.LoggerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import java.net.URI;

@org.springframework.web.bind.annotation.ControllerAdvice
@RequiredArgsConstructor
public class ControllerAdvice {
    private final LoggerService loggerService;
    @ExceptionHandler(InValidRegisterUserException.class)
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public ModelAndView handle(Exception e) {
//        loggerService.errorLoggerTest(e.getMessage());
//        return new ModelAndView("redirect:/", "msg", "검증 실패입니다. 관리자에게 문의하세요.");
//
//    }
    //잘못된 방법 >> ResponseEntity를 넘겨주면 안됨
    public ResponseEntity<String> handle(Exception e) {
        loggerService.errorLoggerTest(e.getMessage());
        // redirect
        String result = e.getMessage();
        return new ResponseEntity<>(result,HttpStatus.BAD_REQUEST);
    }
}
