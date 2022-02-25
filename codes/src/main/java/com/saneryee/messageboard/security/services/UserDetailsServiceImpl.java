package com.saneryee.messageboard.security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saneryee.messageboard.models.User;
import com.saneryee.messageboard.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  @Autowired
  UserRepository userRepository;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String usernameOrEmail ) throws UsernameNotFoundException {
    //User user = userRepository.findByUsername(usernameOrEmail)
    //   .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
    User user = userRepository.findByUsernameOrEmail(usernameOrEmail)
        .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + usernameOrEmail));

    return UserDetailsImpl.build(user);
  }

}
