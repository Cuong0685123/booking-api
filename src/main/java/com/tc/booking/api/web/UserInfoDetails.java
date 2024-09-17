package com.tc.booking.api.web;

import com.tc.booking.model.entity.Account;
import com.tc.booking.model.entity.Customer;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.GrantedAuthority;

public class UserInfoDetails implements UserDetails {

    private final Account account;

    public UserInfoDetails(Account account) {
        this.account = account;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Return authorities based on your Account roles
        // Example implementation: adjust based on your actual roles/authorities
        return List.of(new SimpleGrantedAuthority("ROLE_USER")); // Adjust as needed
    }

    @Override
    public String getPassword() {
        return account.getPassword();
    }

    @Override
    public String getUsername() {
        return account.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Adjust based on your account expiration logic
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Adjust based on your account lock logic
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Adjust based on your credentials expiration logic
    }

    @Override
    public boolean isEnabled() {
        return true; // Adjust based on your account enabled logic
    }

    // Add method to retrieve the Customer from Account
    public Customer getCustomer() {
        return account.getCustomer(); // Ensure this method is available in Account
    }
}
