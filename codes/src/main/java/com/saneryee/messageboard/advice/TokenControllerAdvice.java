package com.saneryee.messageboard.security.jwt.advice;

import com.saneryee.messageboard.security.jwt.exception.TokenRefreshException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

/**
 * @ Author: Jason Zhang
 * @ Created on 2/26/22
 * @ Class: TokenControllerAdvice.java
 * @ Description:
 * @ Copyright (c) 2022 by Jason Zhang/Saneryee, All Rights Reserved.
 */

@RestControllerAdvice
public class TokenControllerAdvice {
    @ExceptionHandler(value = TokenRefreshException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorMessage handleTokenRefreshException(TokenRefreshException ex, WebRequest request) {
        return new ErrorMessage(
                HttpStatus.FORBIDDEN.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false)
        );
    }
}
