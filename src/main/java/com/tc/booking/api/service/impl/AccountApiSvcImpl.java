package com.tc.booking.api.service.impl;

import com.tc.booking.api.service.AccountApiSvc;
import com.tc.booking.model.entity.Account;
import com.tc.booking.repo.AccountDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service("accountApiSvc")
public class AccountApiSvcImpl implements AccountApiSvc {

    @Lazy
    @Autowired
    private AccountDAO accountDAO;

    @Lazy
    @Autowired
    private PasswordEncoder encoder;

    @Override
    public Account addAccount(Account account) {
        account.setPassword(encoder.encode(account.getPassword())); // Encode password before saving
        accountDAO.save(account);
        return accountDAO.findByUsername(account.getUsername()).orElse(null);
    }

    @Override
    public String encodePassword(String password) {
        return encoder.encode(password);
    }

    @Override
    public boolean isPasswordMatched(String rawPwd, String encodedPwd) {
        return encoder.matches(rawPwd, encodedPwd);
    }

    @Override
    public void saveAccount(Account account) {
        accountDAO.save(account);
    }

    @Override
    public Account findByUsername(String username) {
        return accountDAO.findByUsername(username).orElse(null);
    }
}
