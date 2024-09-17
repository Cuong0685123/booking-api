package com.tc.booking.api.proxy.impl;

import com.tc.booking.api.JwtHelper;
import com.tc.booking.api.exception.ApiException;
import com.tc.booking.api.model.comm.ApiResponse;
import com.tc.booking.api.model.comm.LoginReq;
import com.tc.booking.api.model.comm.RegisterRequest;
import com.tc.booking.api.proxy.AuthProxy;
import com.tc.booking.api.service.AccountApiSvc;
import com.tc.booking.exception.BookingException;
import com.tc.booking.model.entity.Account;
import com.tc.booking.model.entity.Customer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.ArrayList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;

@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = {Throwable.class})
@Service("authProxy")
@Slf4j
public class AuthProxyImpl implements AuthProxy {

  @PersistenceContext
  private EntityManager entityManager;

  @Lazy
  @Autowired
  private AccountApiSvc accountApiSvc;

  @Lazy
  @Autowired
  private JwtHelper jwtHelper;

  @Override
  public ApiResponse login(LoginReq req) throws BookingException {
    log.info("Login: username={}", req.getUsername());

    // Tìm kiếm tài khoản theo username từ bảng Account
    TypedQuery<Account> accountQuery = entityManager.createQuery(
        "SELECT a FROM Account a WHERE a.username = :username", Account.class);
    accountQuery.setParameter("username", req.getUsername());
    Account account = accountQuery.getResultStream().findFirst().orElse(null);

    if (account == null) {
      log.error("Username doesn't exist: {}", req.getUsername());
      throw new ApiException("Username or password not correct");
    }

    // So sánh mật khẩu
    if (!accountApiSvc.isPasswordMatched(req.getPassword(), account.getPassword())) {
      log.error("User {} // password does not match.", req.getUsername());
      throw new ApiException("Username or password not correct");
    }

    // Tạo JWT token
    String jwtToken = jwtHelper.genJwtToken(req.getUsername(), new LinkedHashMap<>());
    ApiResponse res = new ApiResponse();
    res.getData().put("token", jwtToken);
    return res;
  }

  @Override
  public ApiResponse register(RegisterRequest req) throws BookingException {
    try {
      // Kiểm tra xem tài khoản đã tồn tại chưa
      TypedQuery<Account> accountQuery = entityManager.createQuery(
          "SELECT a FROM Account a WHERE a.username = :username", Account.class);
      accountQuery.setParameter("username", req.getAccount().getUsername());
      Account existingAccount = accountQuery.getResultStream().findFirst().orElse(null);

      if (existingAccount != null) {
        log.error("Username already exists: {}", req.getAccount().getUsername());
        throw new ApiException("Username already exists");
      }

      // Tạo và lưu tài khoản mới
      Account account = new Account();
      account.setUsername(req.getAccount().getUsername());
      String encodedPassword = accountApiSvc.encodePassword(req.getAccount().getPassword());
      account.setPassword(encodedPassword);

      // Lưu tài khoản mới vào cơ sở dữ liệu
      entityManager.persist(account);

      // Tạo và lưu khách hàng mới
      Customer customer = new Customer();
      customer.setName(req.getName());
      customer.setPhone(req.getPhone());
      customer.setEmail(req.getEmail());
      customer.setBookings(new ArrayList<>()); // Khởi tạo danh sách đặt phòng

      // Liên kết khách hàng với tài khoản
      customer.setAccount(account);

      // Lưu thông tin khách hàng
      entityManager.persist(customer);

      ApiResponse res = new ApiResponse();
      res.getData().put("message", "Registration successful");
      return res;

    } catch (Exception e) {
      log.error("Registration failed", e);
      throw new BookingException("Registration failed", e);
    }
  }
}
