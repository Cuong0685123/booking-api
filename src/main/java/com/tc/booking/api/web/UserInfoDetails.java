/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tc.booking.api.web;

import com.tc.booking.model.entity.User;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author binh
 */
public class UserInfoDetails implements UserDetails {

  // Changed from 'name' to 'username' for clarity
  private String username;
  private String password;
  private List<GrantedAuthority> authorities;

  public UserInfoDetails(User userInfo,
      List<String> roles) {
    this.username = userInfo.getLogin();
    this.password = userInfo.getPassword();
    this.authorities = roles
        .stream()
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toList());
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true; // Implement your logic if you need this
  }

  @Override
  public boolean isAccountNonLocked() {
    return true; // Implement your logic if you need this
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true; // Implement your logic if you need this
  }

  @Override
  public boolean isEnabled() {
    return true; // Implement your logic if you need this
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }
}
