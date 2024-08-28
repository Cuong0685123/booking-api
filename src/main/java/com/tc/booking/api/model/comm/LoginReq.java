/**
 *
 */
package com.tc.booking.api.model.comm;

import lombok.Getter;
import lombok.Setter;

/**
 * @author binh
 *
 */
@Setter
@Getter
public class LoginReq extends ApiRequest {

  private String username;
  private String password;
}
