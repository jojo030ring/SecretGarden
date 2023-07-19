package com.yezi.secretgarden.exception;

public class InValidPwException extends Exception {
    public InValidPwException() {
        super("동일한 패스워드입니다. 다시 시도해주세요.");
    }
}
