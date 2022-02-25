package com.saneryee.messageboard.security.services;

/**
 * @ Author: Jason Zhang
 * @ Created on 2/26/22
 * @ Class: RefreshTokenRepository.java
 * @ Description:
 * @ Copyright (c) 2022 by Jason Zhang/Saneryee, All Rights Reserved.
 */

import com.saneryee.messageboard.models.RefreshToken;
import com.saneryee.messageboard.repository.RefreshTokenRepository;
import com.saneryee.messageboard.repository.UserRepository;
import com.saneryee.messageboard.security.jwt.exception.TokenRefreshException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.beans.Transient;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;


@Service
public class RefreshTokenService {
    @Value("{saneryee.app.jwtRefreshExpirationMS")
    private long refreshTokenDurationMS;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private UserRepository userRepository;

    public Optional<RefreshToken>findByToken(String token){
        return refreshTokenRepository.findByToken(token);
    }
    public RefreshToken createRefreshToken(Long userId){
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(userRepository.findById(userId).get());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMS));
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken =  refreshTokenRepository.save(refreshToken);

        return refreshToken;
    }

    public RefreshToken verifyRefreshToken(RefreshToken token){

        if(token.getExpiryDate().compareTo(Instant.now())<0){
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request.");
        }
        return token;
    }
    @Transactional
    public int deleteByUserId(Long userId){
        return refreshTokenRepository.deleteByUser(userRepository.findById(userId).get());
    }


}
