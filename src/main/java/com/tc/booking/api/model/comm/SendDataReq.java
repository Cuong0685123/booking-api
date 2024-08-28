/**
 *
 */
package com.tc.booking.api.model.comm;

import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SendDataReq extends ApiRequest {

  private Map<String, Object> data;
  private Map<String, Object> options;

  public SendDataReq() {
    super();
  }

}
