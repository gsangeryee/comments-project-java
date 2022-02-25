package com.saneryee.messageboard.payload.request;

import javax.validation.constraints.NotBlank;

/**
 * @ Author: Jason Zhang
 * @ Created on 2/25/22
 * @ Class: TokenRefreshRequest.java
 * @ Description: Refresh Token payload for REST API
 * @ Copyright (c) 2022 by Jason Zhang/Saneryee, All Rights Reserved.
 */
public class TokenRefreshRequest {
    @NotBlank
    private String refreshToken;

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
