package com.tc.booking.api.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.tc.booking.model.entity.Account;
import com.tc.booking.model.entity.Customer;
import com.tc.booking.repo.AccountDAO;
import com.tc.booking.repo.CustomerDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class RegistrationController {

    @Autowired
    private AccountDAO accountDAO;

    @Autowired
    private CustomerDAO customerDAO;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Customer customer) {
        // Kiểm tra xem tên người dùng đã tồn tại chưa
        Optional<Account> existingAccount = accountDAO.findByUsername(customer.getAccount().getUsername());
        if (existingAccount.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Tên người dùng đã tồn tại");
        }

      

        // Lưu khách hàng và tài khoản
        customerDAO.save(customer);

        return ResponseEntity.status(HttpStatus.CREATED).body("Đăng ký thành công");
    }

   @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody JsonNode loginRequest) {
        String username = loginRequest.get("username").asText();
        String password = loginRequest.get("password").asText();

        // Kiểm tra thông tin đăng nhập
        Optional<Account> accountOpt = accountDAO.findByUsername(username);
        if (accountOpt.isPresent()) {
            Account account = accountOpt.get();
            // Kiểm tra mật khẩu
            if (password.equals(account.getPassword())) {
                return ResponseEntity.ok("Đăng nhập thành công");
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Tên người dùng hoặc mật khẩu không đúng");
    }
}