/**
 *
 */
package com.tc.booking.api.model.comm;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class ApiResponse {

  public static final String RES_LIST = "list";
  public static final String RES_COUNT = "count";
  public static final String RES_ENTITY = "entity";
  public static final String RES_DELETED = "deleted";
  public static final String B_NEW = "bNew";
  private List<ApiError> errors;
  private Map<String, Object> data;

  @JsonIgnore
  private Map<String, Object> extData;

  /**
   * constructor.
   */
  public ApiResponse() {

  }

  /**
   * @return the data
   */
  public Map<String, Object> getData() {
    if (data == null) {
      data = new LinkedHashMap<>();
    }
    return data;
  }

  /**
   * @return the errors
   */
  public List<ApiError> getErrors() {
    if (errors == null) {
      errors = new ArrayList<>();
    }
    return errors;
  }

  /**
   * @return the extData
   */
  public Map<String, Object> getExtData() {
    if (extData == null) {
      extData = new LinkedHashMap<>();
    }
    return extData;
  }
}
