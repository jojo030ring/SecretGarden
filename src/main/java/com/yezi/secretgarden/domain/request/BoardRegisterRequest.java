package com.yezi.secretgarden.domain.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
public class BoardRegisterRequest {
    @NotBlank // null 및 공백처리
    String id;
    @NotBlank
    String title;
    @NotBlank
    String content;
}
