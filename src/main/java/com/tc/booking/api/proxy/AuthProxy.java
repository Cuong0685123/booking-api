/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.tc.booking.api.proxy;

import com.tc.booking.api.model.comm.ApiResponse;
import com.tc.booking.api.model.comm.LoginReq;
import com.tc.booking.exception.BookingException;

/**
 *
 * @author binh
 */
public interface AuthProxy {

  ApiResponse login(LoginReq req)
      throws BookingException;
}
