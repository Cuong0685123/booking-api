/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tc.booking.api.controller;

import com.tc.booking.api.model.comm.ApiResponse;
import com.tc.booking.api.model.comm.LoginReq;
import com.tc.booking.api.proxy.AuthProxy;
import com.tc.booking.exception.BookingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author binh
 */
@RestController
public class AuthController {

  @Lazy
  @Autowired
  private AuthProxy authProxy;

  @PostMapping("/login")
  public ApiResponse login(@RequestBody LoginReq req)
      throws BookingException {
    return authProxy.login(req);
  }
}
