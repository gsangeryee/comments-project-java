package com.saneryee.messageboard.payload.response;

/**
 * @ Author: Jason Zhang
 * @ Created on 2/25/22
 * @ Class: TokenRefreshResponse.java
 * @ Description: For the RestAPIs, response payloads
 * @ Copyright (c) 2022 by Jason Zhang/Saneryee, All Rights Reserved.
 */
public class TokenRefreshResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";

    public TokenRefreshResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }
}
