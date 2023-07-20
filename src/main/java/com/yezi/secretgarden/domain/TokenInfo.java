package com.yezi.secretgarden.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class TokenInfo {
    private String grantType;
    private String accessToken;

}
