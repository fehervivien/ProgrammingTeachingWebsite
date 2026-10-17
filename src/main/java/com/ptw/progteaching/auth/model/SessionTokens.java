package com.ptw.progteaching.auth.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SessionTokens {

    private String accessToken;
    private String refreshToken;
    private Long userId;
    private String username;

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
