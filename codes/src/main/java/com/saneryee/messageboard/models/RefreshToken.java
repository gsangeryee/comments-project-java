package com.saneryee.messageboard.models;

import org.springframework.stereotype.Controller;

import javax.persistence.*;
import java.time.Instant;

/**
 * @ Author: Jason Zhang
 * @ Created on 2/25/22
 * @ Class: RefreshToken.java
 * @ Description: One-to-one relationship between RefreshToken and User
 * @ Copyright (c) 2022 by Jason Zhang/Saneryee, All Rights Reserved.
 */
@Entity(name = "refreshtoken")
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    @Column(nullable = false, unique = true)
    private String token;
    @Column(nullable = false)
    private Instant expiryDate;
    //getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Instant getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Instant expiryDate) {
        this.expiryDate = expiryDate;
    }
}
