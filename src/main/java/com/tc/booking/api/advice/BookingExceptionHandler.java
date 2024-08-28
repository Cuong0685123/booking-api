/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tc.booking.api.advice;

import com.tc.booking.api.model.comm.ApiError;
import com.tc.booking.api.model.comm.ApiResponse;
import com.tc.booking.exception.BookingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 *
 * @author binh
 */
@ControllerAdvice
@Slf4j
public class BookingExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler({BookingException.class})
  protected ResponseEntity<ApiResponse> handleBookingException(
      BookingException ex,
      WebRequest req) {
    log.error("Handle API exception", ex);
    ApiResponse body = new ApiResponse();
    ApiError err = new ApiError(ex.getMessage());
    err.setCode(ex.getErrCode());
    body.getErrors()
        .add(err);
    ResponseEntity<ApiResponse> res = new ResponseEntity<>(body, HttpStatus.OK);
    return res;
  }
}
