package com.yezi.secretgarden.exception;

public class InValidRegisterUserException extends RuntimeException{
    public InValidRegisterUserException() {
        super("입력 검증에 실패하였습니다. 다시 확인해주세요.");

    }
}
