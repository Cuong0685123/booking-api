package com.tc.booking.api.service;

import com.tc.booking.model.entity.Account;

public interface AccountApiSvc {
  
  Account addAccount(Account account);

  String encodePassword(String password);

  boolean isPasswordMatched(String rawPwd, String encodedPwd);
  
  void saveAccount(Account account);
  
  Account findByUsername(String username);
}
