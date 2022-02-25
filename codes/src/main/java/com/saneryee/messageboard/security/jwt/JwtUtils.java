package com.saneryee.messageboard.security.jwt;

import java.security.Key;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.saneryee.messageboard.security.services.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

/**
  *@author Jason Zhang
  *@date 2022/2/16
  *@description Generatea JWT token using username,date,expiration time,secret key.
  *             Get usename from a JWT token
  *             Validate a JWT token
 */

@Component
public class JwtUtils {
  private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

  @Value("${saneryee.app.jwtSecretKey}")
  private String jwtSecretKey;

  @Value("${saneryee.app.jwtExpirationMs}")
  private int jwtExpirationMs;

  // New version  in JJWT 0.11
  byte[] keyBytes = Decoders.BASE64.decode(jwtSecretKey);

  Key key = Keys.hmacShaKeyFor(keyBytes);

  /**
   * Generate a JWT token using UserDetailsImpl
   * @param userPrincipal
   * @return JWT token
   */
  public String generateJwtToken(UserDetailsImpl userPrincipal){
    return generateTokenFromUsername(userPrincipal.getUsername());
  }

  /**
   * Generate a JWT token using usernaame,date,expiration time,secret key.
   * @param username
   * @return  JWT token
   */
  public String generateTokenFromUsername(String username){
    return Jwts.builder().setSubject(username)
            .setIssuedAt(new Date())
            .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
            .signWith(key)
            .compact();
  }
  /**
   * Get Username from a JWT token
   * @param token
   * @return username
   */
  public String getUserNameFromJwtToken(String token) {
    // return Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(token).getBody().getSubject(); JJWT<=0.99
    return Jwts.parserBuilder().setSigningKey(jwtSecretKey).build().parseClaimsJws(token).getBody().getSubject();
  }

  /**
   * Validate a JWT token
   * @param authToken
   * @return
   */
  public boolean validateJwtToken(String authToken) {
    try {
      // Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(authToken); JJWT<=0.99
      Jwts.parserBuilder().setSigningKey(jwtSecretKey).build().parseClaimsJws(authToken);
      return true;
    } catch (SignatureException e) {
      logger.error("Invalid JWT signature: {}", e.getMessage());
    } catch (MalformedJwtException e) {
      logger.error("Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      logger.error("JWT token is expired: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      logger.error("JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      logger.error("JWT claims string is empty: {}", e.getMessage());
    }

    return false;
  }
}
