package com.tc.booking.api.web;

import com.tc.booking.api.service.AccountApiSvc;
import com.tc.booking.model.entity.Account;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserInfoService implements UserDetailsService {

    private final AccountApiSvc accountApiSvc;

    public UserInfoService(AccountApiSvc accountApiSvc) {
        this.accountApiSvc = accountApiSvc;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountApiSvc.findByUsername(username);
        if (account == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new UserInfoDetails(account);
    }
}
