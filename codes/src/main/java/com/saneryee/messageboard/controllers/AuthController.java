package com.saneryee.messageboard.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.crypto.KeyGenerator;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.saneryee.messageboard.models.RefreshToken;
import com.saneryee.messageboard.payload.request.LogOutRequest;
import com.saneryee.messageboard.payload.request.TokenRefreshRequest;
import com.saneryee.messageboard.payload.response.TokenRefreshResponse;
import com.saneryee.messageboard.exception.TokenRefreshException;
import com.saneryee.messageboard.security.services.RefreshTokenService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.NullRememberMeServices;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saneryee.messageboard.models.ERole;
import com.saneryee.messageboard.models.Role;
import com.saneryee.messageboard.models.User;
import com.saneryee.messageboard.payload.request.LoginRequest;
import com.saneryee.messageboard.payload.request.SignupRequest;
import com.saneryee.messageboard.payload.response.JwtResponse;
import com.saneryee.messageboard.payload.response.MessageResponse;
import com.saneryee.messageboard.repository.RoleRepository;
import com.saneryee.messageboard.repository.UserRepository;
import com.saneryee.messageboard.security.jwt.JwtUtils;
import com.saneryee.messageboard.security.services.UserDetailsImpl;

/**
 * @author Jason Zhang
 * @date 2022/2/16
 * @description Define a controller that handle authentication.
 * – /api/auth/signup
 *   - check existing username/email
 *   - create new User (with ROLE_USER if not specifying role)
 *   - save User to database using UserRepository
 * – /api/auth/login
 *   - authenticate { username, pasword }
 *   - update SecurityContext using Authentication object
 *   - generate JWT
 *   - get UserDetails from Authentication object
 *   - response contains JWT and UserDetails data
*/

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {
  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;

  @Autowired
  RefreshTokenService refreshTokenService;

  @Value("${saneryee.app.jwtSecretKey}")
  private String jwtSecretKey;

  @Value("${saneryee.app.salt}")
  private String serverSalt;

  @PostMapping("/login")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

    // authenticate user with username and password
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

    // generate access token using jwt utils
    String accessToken = jwtUtils.generateJwtToken(userDetails);

    List<String> roles = userDetails.getAuthorities().stream()
        .map(item -> item.getAuthority())
        .collect(Collectors.toList());

    // TODO: add remember me control: if remember me is checked, generate refresh token and save it to database
    // generate refresh token
    RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
    // Using AES to encrypt refresh token
    //String salt = KeyGenerators.string().generateKey();
    //log.info("salt: " + salt);
    TextEncryptor encryptor = Encryptors.text(jwtSecretKey,serverSalt);
    String encryptedRefreshToken = encryptor.encrypt(refreshToken.getToken());


    return ResponseEntity.ok(
            new JwtResponse(accessToken,
                    encryptedRefreshToken,
                    userDetails.getId(),
                    userDetails.getUsername(),
                    userDetails.getEmail(),
                    roles));
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      return ResponseEntity
          .badRequest()
          .body(new MessageResponse("Error: Username is already taken!"));
    }

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity
          .badRequest()
          .body(new MessageResponse("Error: Email is already in use!"));
    }

    // Create new user's account
    User user = new User(signUpRequest.getUsername(), 
               signUpRequest.getEmail(),
               encoder.encode(signUpRequest.getPassword()));

    Set<String> strRoles = signUpRequest.getRole();
    Set<Role> roles = new HashSet<>();

    if (strRoles == null) {
      Role userRole = roleRepository.findByName(ERole.ROLE_USER)
          .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
      roles.add(userRole);
    } else {
      strRoles.forEach(role -> {
        switch (role) {
        case "admin":
          Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(adminRole);

          break;
        case "mod":
          Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(modRole);

          break;
        default:
          Role userRole = roleRepository.findByName(ERole.ROLE_USER)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(userRole);
        }
      });
    }

    user.setRoles(roles);
    userRepository.save(user);

    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
  }

  /**
   * Refresh token
   * @param request
   * @return
   */
  @PostMapping("/refreshtoken")
  public ResponseEntity<?>refreshtoken(@Valid @RequestBody TokenRefreshRequest request){
    // Get refresh token from request
    String requestRefreshToken = request.getRefreshToken();
    TextEncryptor decryptor = Encryptors.text(jwtSecretKey,serverSalt);
    String decryptedRefreshToken = decryptor.decrypt(requestRefreshToken);
    log.info("Decrypted refresh token: " + decryptedRefreshToken);
    // 1. Get the RefreshToken object {id, user, token, expiryDate} from database using RefreshTokenService
    // 2. Verify the refresh token (expired or not) basing on the expiryDate.
    // 3. If the refresh token is valid, generate a new access token using JwtUtils with user.
    // 4. Return TokenResponse with access token and refresh token.
    // 5. If the refresh token is not valid, return an error message.

    return refreshTokenService.findByToken(decryptedRefreshToken)
            .map(refreshTokenService::verifyRefreshToken)
            .map(RefreshToken::getUser)// get user field from RefreshToken object
            .map(user -> {
              String token = jwtUtils.generateTokenFromUsername(user.getUsername());
              return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
            })
            .orElseThrow(() -> new TokenRefreshException(
                    requestRefreshToken, "Refresh token is not in database!"));

  }

  @PostMapping("/logout")
  public ResponseEntity<?> logoutUser(@Valid @RequestBody LogOutRequest logOutRequest){
    refreshTokenService.deleteByUserId(logOutRequest.getUserId());
    return ResponseEntity.ok(new MessageResponse("User logged out successfully!"));
  }
}
