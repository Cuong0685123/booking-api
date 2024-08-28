/**
 *
 */
package com.tc.booking.api.model.comm;

import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QueryDataReq extends ApiRequest {

  private String search;
  private int start;
  private int limit;
  private Map<String, Object> params;
  private Map<String, Object> options;

  public QueryDataReq() {
    limit = 10;
  }

}
