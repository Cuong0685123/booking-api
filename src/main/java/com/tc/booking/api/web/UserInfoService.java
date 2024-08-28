/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tc.booking.api.web;

import com.tc.booking.model.entity.User;
import com.tc.booking.repo.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author binh
 */
@Service
public class UserInfoService implements UserDetailsService {

  @Lazy
  @Autowired
  private UserRepository userRepo;

  @Lazy
  @Autowired
  private PasswordEncoder encoder;

  @Override
  public UserDetails loadUserByUsername(String username)
      throws AuthenticationException {
    User user = userRepo.findByLogin(username)
        .orElse(null);
    if (user == null) {
      throw new UsernameNotFoundException("User not found: " + username);
    }
    if (!user.isActive()) {
      throw new AuthenticationServiceException("User not active: " + username);
    }
    List<String> roles = new ArrayList<>();
    roles.add("ROLE_USER");
    if (Objects.equals(username, "admin")) {
      roles.add("ROLE_ADMIN");
    }
    return new UserInfoDetails(user, roles);
  }

}
