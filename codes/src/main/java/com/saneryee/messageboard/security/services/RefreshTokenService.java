package com.saneryee.messageboard.security.services;

/**
 * @ Author: Jason Zhang
 * @ Created on 2/26/22
 * @ Class: RefreshTokenRepository.java
 * @ Description: Provide several refresh token methods:
 * @              findByToken(), createRefreshToken(), verifyExpiration().
 * @ Copyright (c) 2022 by Jason Zhang/Saneryee, All Rights Reserved.
 */

import com.saneryee.messageboard.models.RefreshToken;
import com.saneryee.messageboard.repository.RefreshTokenRepository;
import com.saneryee.messageboard.repository.UserRepository;
import com.saneryee.messageboard.exception.TokenRefreshException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.beans.Transient;
import java.security.DigestException;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class RefreshTokenService {
    @Value("${saneryee.app.jwtRefreshExpirationMS}")
    private Long refreshTokenDurationMS;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private UserRepository userRepository;

    /**
     * @param token
     * @return refresh token
     * @Description: find refresh token by token
     */
    public Optional<RefreshToken>findByToken(String token){

        return refreshTokenRepository.findByToken(token);
    }

    /**
     * @Description: create, save(database) and return a new refresh token
     * TODO: Whether use Jwt to gernerate refresh token?
     */
    public RefreshToken createRefreshToken(Long userId){
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(userRepository.findById(userId).get());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMS));
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken =  refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    /**
     * @param token refresh token
     * @Description: Verify whether the token provided has expired or not.
     *               If the token was expired, delete it from database
     *               and throw TokenRefreshException
     */
    public RefreshToken verifyRefreshToken(RefreshToken token){

        if(token.getExpiryDate().compareTo(Instant.now())<0){
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(),
                    "Refresh token was expired. Please make a new login request.");
        }
        return token;
    }

    @Transactional
    public int deleteByUserId(Long userId){
        return refreshTokenRepository.deleteByUser(userRepository.findById(userId).get());
    }


}
