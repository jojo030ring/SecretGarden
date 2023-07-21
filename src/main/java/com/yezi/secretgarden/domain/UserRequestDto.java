package com.yezi.secretgarden.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserRequestDto {
    private String username;
    private String password;
}
