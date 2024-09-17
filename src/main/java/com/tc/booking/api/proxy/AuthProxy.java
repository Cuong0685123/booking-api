package com.tc.booking.api.proxy;

import com.tc.booking.api.model.comm.ApiResponse;
import com.tc.booking.api.model.comm.LoginReq;
import com.tc.booking.api.model.comm.RegisterRequest;  
import com.tc.booking.exception.BookingException;

/**
 *
 * @author binh
 */
public interface AuthProxy {

  ApiResponse login(LoginReq req) throws BookingException;

  ApiResponse register(RegisterRequest req) throws BookingException;  
}
