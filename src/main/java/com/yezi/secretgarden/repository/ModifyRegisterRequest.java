package com.yezi.secretgarden.repository;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
@Getter
@Setter
@ToString
public class ModifyRegisterRequest {
    @NotBlank
    @Size(min=10, max=20)
    // 영문, 숫자, 특수기호 조합 8자리 이상
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{10,20}$")
    String pw;
    @Pattern(regexp = "^[a-zA-Z0-9+-_.]+")
    @NotBlank
    String email_id;
    @Pattern(regexp="[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")
    @NotBlank
    String user_domain;
    @Pattern(regexp = "^\\d{2,3}-?\\d{3,4}-?\\d{4}$")
    @NotBlank
    String phonenum;
}
