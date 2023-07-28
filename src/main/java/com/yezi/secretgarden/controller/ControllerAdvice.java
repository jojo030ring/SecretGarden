package com.yezi.secretgarden.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;

@org.springframework.web.bind.annotation.ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class ControllerAdvice {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handle(BindException e) {
        BindingResult b = e.getBindingResult();
        for(ObjectError error : b.getAllErrors()) {
            log.error(error.toString());
        }
        log.error(e.getMessage());
        // redirect
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
//    @ExceptionHandler(UsernameNotFoundException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ResponseEntity<String> authenticationFail(BindException e) {
//        loggerService.errorLoggerTest("exception !!");
//
//        BindingResult b = e.getBindingResult();
//        for(ObjectError error : b.getAllErrors()) {
//            loggerService.errorLoggerTest(error.toString());
//        }
//        loggerService.errorLoggerTest(e.getMessage());
//        // redirect
//        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> duplicateMember(Exception e) {

        log.error(e.getMessage());
        String result = "중복된 회원입니다. 다시 시도하세요.";

        // redirect
        return new ResponseEntity<String>(result, HttpStatus.BAD_REQUEST);
    }

}
