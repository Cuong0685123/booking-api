/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tc.booking.api;

import com.tc.booking.api.web.JwtAuthFilter;
import com.tc.booking.api.web.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 *
 * @author binh
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

  @Autowired
  private JwtAuthFilter authFilter;

  @Bean
  public UserDetailsService userDetailsService() {
    return new UserInfoService(); // Ensure UserInfoService implements UserDetailsService
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http)
      throws Exception {
    http
        .csrf(csrf -> csrf.disable()) // Disable CSRF for stateless APIs
        .authorizeHttpRequests(auth -> auth
        .requestMatchers(
            "/hello",
            "/auth/welcome",
            "/login",
            "/booking/**",
            "/auth/generateToken")
        .permitAll()
        .requestMatchers("/backoffice/**")
        .hasAuthority("ROLE_USER")
        .requestMatchers("/admin/**")
        .hasAuthority("ROLE_ADMIN")
        .anyRequest()
        .authenticated() // Protect all other endpoints
        )
        .sessionManagement(sess -> sess
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // No sessions
        )
        .authenticationProvider(authenticationProvider()) // Custom authentication provider
        .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class); // Add JWT filter

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(); // Password encoding
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
    authenticationProvider.setUserDetailsService(userDetailsService());
    authenticationProvider.setPasswordEncoder(passwordEncoder());
    return authenticationProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
      throws Exception {
    return config.getAuthenticationManager();
  }
}
