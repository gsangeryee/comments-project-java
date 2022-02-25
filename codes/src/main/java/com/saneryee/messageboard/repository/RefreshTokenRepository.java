package com.saneryee.messageboard.repository;

import com.saneryee.messageboard.models.RefreshToken;
import com.saneryee.messageboard.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;

/**
 * @ Author: Jason Zhang
 * @ Created on 2/25/22
 * @ Class: RefreshTokenRepository.java
 * @ Description:
 * @ Copyright (c) 2022 by Jason Zhang/Saneryee, All Rights Reserved.
 */
public interface RefreshTokenRepository  extends JpaRepository<RefreshToken, Long> {
    @Override
    Optional<RefreshToken> findById(Long id);
    Optional<RefreshToken> findByToken(String token);

    @Modifying
    int deleteByUser(User user);

}
