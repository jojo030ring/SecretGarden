package com.yezi.secretgarden.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class UserRegisterRequest {
    private String id;
    private String password;
    private String name;
    private String email_id;
    private String user_domain;
    private String phonenum;
}
