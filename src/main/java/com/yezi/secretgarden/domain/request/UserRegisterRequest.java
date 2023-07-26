package com.yezi.secretgarden.domain.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Setter
@Getter
@ToString

public class UserRegisterRequest {
    @NotBlank
    @Size(min=4, max=10)
    // 영소문자와 숫자의 조합
    @Pattern(regexp = "^[A-Za-z0-9_\\-]{4,10}$")
    private String id;
    @NotBlank
    @Size(min=8, max=16)
    // 영문, 숫자, 특수기호 조합 8자리 이상
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,16}$")
    private String password;
    @Size(min=2, max=10)
    @Pattern(regexp = "^[가-힣a-zA-Z]+$")
    @NotBlank
    private String name;
    @Pattern(regexp = "^[a-zA-Z0-9+-_.]+")
    @NotBlank
    private String email_id;
    @Pattern(regexp="[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")
    @NotBlank
    private String user_domain;
    @Pattern(regexp = "^\\d{2,3}-?\\d{3,4}-?\\d{4}$")
    @NotBlank
    private String phonenum;
}
