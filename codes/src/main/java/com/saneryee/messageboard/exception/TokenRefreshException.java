package com.saneryee.messageboard.security.jwt.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @ Author: Jason Zhang
 * @ Created on 2/26/22
 * @ Class: TokenRefreshException.java
 * @ Description: Create a  TokenRefreshException class that extends RuntimeException.
 * @ Copyright (c) 2022 by Jason Zhang/Saneryee, All Rights Reserved.
 */

@ResponseStatus(HttpStatus.FORBIDDEN)
public class TokenRefreshException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public TokenRefreshException(String token, String message) {
        super(String.format("Failed for [%s]: %s", token, message));
    }
}
