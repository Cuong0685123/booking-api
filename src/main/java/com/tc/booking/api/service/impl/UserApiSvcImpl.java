/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tc.booking.api.service.impl;

import com.tc.booking.api.service.UserApiSvc;
import com.tc.booking.model.entity.User;
import com.tc.booking.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author binh
 */
@Service("userApiSvc")
public class UserApiSvcImpl implements UserApiSvc {

  @Lazy
  @Autowired
  private UserRepository userRepo;

  @Lazy
  @Autowired
  private PasswordEncoder encoder;

  @Override
  public User addUser(User user) {
    // Encode password before saving the user
    user.setPassword(encoder.encode(user.getPassword()));
    userRepo.save(user);
    user = userRepo.findByLogin(user.getLogin())
        .orElse(null);
    return user;
  }

  @Override
  public boolean isPasswordMatched(String rawPwd,
      String encodedPwd) {
    return encoder.matches(rawPwd, encodedPwd);
  }

}
