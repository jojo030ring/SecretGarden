package com.yezi.secretgarden.controller;

import com.yezi.secretgarden.exception.InValidRegisterUserException;
import com.yezi.secretgarden.service.LoggerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.URI;

@org.springframework.web.bind.annotation.ControllerAdvice
@RequiredArgsConstructor
public class ControllerAdvice {
    private final LoggerService loggerService;
    @ExceptionHandler(InValidRegisterUserException.class)
    public ResponseEntity<String> handle(Exception e) {
        loggerService.errorLoggerTest(e.getMessage());
        // redirect
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/error"));
        return new ResponseEntity<>(headers,HttpStatus.BAD_REQUEST);
    }
}
