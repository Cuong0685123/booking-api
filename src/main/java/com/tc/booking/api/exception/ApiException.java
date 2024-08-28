/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tc.booking.api.exception;

import com.tc.booking.exception.BookingException;

/**
 *
 * @author binh
 */
public class ApiException extends BookingException {

  public ApiException(String message) {
    super(message);
  }

  public ApiException(String message,
      Throwable cause) {
    super(message, cause);
  }

}
