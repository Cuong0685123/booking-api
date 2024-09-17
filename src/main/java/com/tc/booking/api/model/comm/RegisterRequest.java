package com.tc.booking.api.model.comm;

import lombok.Getter;
import lombok.Setter;

/**
 * @author binh
 *
 */
@Setter
@Getter
public class RegisterRequest extends ApiRequest {

  private String name;
  private String phone;
  private String email;
  private AccountRequest account;

  @Setter
  @Getter
  public static class AccountRequest {
    private String username;
    private String password;
  }
}
