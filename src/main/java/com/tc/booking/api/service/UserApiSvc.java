/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tc.booking.api.service;

import com.tc.booking.model.entity.User;

/**
 *
 * @author binh
 */
public interface UserApiSvc {

  User addUser(User user);

  boolean isPasswordMatched(String rawPwd, String encodedPwd);
}
