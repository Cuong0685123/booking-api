/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tc.booking.api.proxy.impl;

import com.tc.booking.api.JwtHelper;
import com.tc.booking.api.exception.ApiException;
import com.tc.booking.api.model.comm.ApiResponse;
import com.tc.booking.api.model.comm.LoginReq;
import com.tc.booking.api.proxy.AuthProxy;
import com.tc.booking.api.service.UserApiSvc;
import com.tc.booking.exception.BookingException;
import com.tc.booking.model.entity.User;
import com.tc.booking.repo.UserRepository;
import java.util.LinkedHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author binh
 */
@Transactional(readOnly = true,
    propagation = Propagation.REQUIRED,
    rollbackFor = {Throwable.class})
@Service("authProxy")
@Slf4j
public class AuthProxyImpl implements AuthProxy {
  
  @Lazy
  @Autowired
  private UserRepository userRepo;
  
  @Lazy
  @Autowired
  private UserApiSvc userApiSvc;
  
  @Lazy
  @Autowired
  private JwtHelper jwtHelper;
  
  @Override
  public ApiResponse login(LoginReq req)
      throws BookingException {    
    log.info("Login: username={}", req.getUsername());    
    User user = userRepo.findByLogin(req.getUsername())
        .orElse(null);
    if (user == null) {
      log.error("Username doesn't exist: {}", req.getUsername());
      throw new ApiException("Username or password not correct");
    }
    
    if (!userApiSvc.isPasswordMatched(req.getPassword(), user.getPassword())) {
      log.error("User {} // password does not match.", req.getUsername());
      throw new ApiException("Username or password not correct");
    }
    
    if (!user.isActive()) {
      log.error("User is not active: {}", req.getUsername());
      throw new ApiException("Username or password not correct");
    }
    
    String jwtToken = jwtHelper.genJwtToken(req.getUsername(), new LinkedHashMap<>());
    ApiResponse res = new ApiResponse();
    res.getData()
        .put("token", jwtToken);
    return res;
  }
  
}
