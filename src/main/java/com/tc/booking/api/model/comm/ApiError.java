/**
 *
 */
package com.tc.booking.api.model.comm;

import java.util.Map;
import lombok.Getter;
import lombok.Setter;

/**
 * Api error class
 */
@Setter
@Getter
public class ApiError {

  private String code;
  private String message;
  private String trace;
  private Map<String, Object> data;

  public ApiError(String msg) {
    this.message = msg;
  }

  public ApiError(String msg,
      String trace) {
    this.message = msg;
    this.trace = trace;
  }
}
