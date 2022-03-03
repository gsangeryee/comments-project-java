package com.saneryee.messageboard.advice;

import java.util.Date;

/**
 * @ Author: Jason Zhang
 * @ Created on 2/26/22
 * @ Class: ErrorMessage.java
 * @ Description:
 * @ Copyright (c) 2022 by Jason Zhang/Saneryee, All Rights Reserved.
 */
public class ErrorMessage {
    private int statusCode;
    private Date timestamp;
    private String message;
    private String description;

    public ErrorMessage(int statusCode, Date timestamp, String message, String description) {
        this.statusCode = statusCode;
        this.timestamp = timestamp;
        this.message = message;
        this.description = description;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
