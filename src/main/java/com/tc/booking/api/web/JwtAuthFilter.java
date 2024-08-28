/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tc.booking.api.web;

import com.tc.booking.api.JwtHelper;
import com.tc.booking.api.exception.ApiException;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 *
 * @author binh
 */
@Component
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

  @Lazy
  @Autowired
  private JwtHelper jwtHelper;

  @Lazy
  @Autowired
  private UserInfoService userDetailsService;

  @Override
  protected void doFilterInternal(HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain)
      throws ServletException, IOException {
    try {
      // Retrieve the Authorization header
      String authHeader = request.getHeader("Authorization");
      String token = null;
      String username = null;

      // Check if the header starts with "Bearer "
      if (authHeader != null && authHeader.startsWith("Bearer ")) {
        token = authHeader.substring(7); // Extract token
//      username = jwtHelper.extractUsername(token); // Extract username from token
        Claims cl = jwtHelper.parseJwtToken(token);
        username = cl.getSubject();
      }

      // If the token is valid and no authentication is set in the context
      if (username != null && SecurityContextHolder.getContext()
          .getAuthentication() == null) {
        // Validate token and set authentication
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            userDetails.getAuthorities()
        );
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext()
            .setAuthentication(authToken);
      }
    } catch (ApiException ex) {
      log.error("Auth error:", ex);
      throw new ServletException("Failed to authenticate.");
    }

    // Continue the filter chain
    filterChain.doFilter(request,
        response);
  }
}
