package com.yezi.secretgarden.domain.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BoardRegisterRequest {
    String id;
    String title;
    String content;
}
