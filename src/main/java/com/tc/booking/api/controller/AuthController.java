package com.tc.booking.api.controller;

import com.tc.booking.api.model.comm.ApiResponse;
import com.tc.booking.api.model.comm.LoginReq;
import com.tc.booking.api.model.comm.RegisterRequest;
import com.tc.booking.api.proxy.AuthProxy;
import com.tc.booking.exception.BookingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.CrossOrigin;
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
   @CrossOrigin(origins = "http://localhost:3000")
  public ApiResponse login(@RequestBody LoginReq req) throws BookingException {
    return authProxy.login(req);
  }

  @PostMapping("/register")
  public ApiResponse register(@RequestBody RegisterRequest req) throws BookingException {
    try {
      // Gọi phương thức đăng ký từ AuthProxy hoặc một dịch vụ khác
      return authProxy.register(req);
    } catch (Exception e) {
      // Xử lý lỗi nếu có
      throw new BookingException("Registration failed: " + e.getMessage());
    }
  }
}
