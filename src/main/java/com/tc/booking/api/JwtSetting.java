/**
 *
 */
package com.tc.booking.api;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author binh
 *
 */
@Configuration
@ConfigurationProperties(prefix = "jwt")
@Setter
@Getter
public class JwtSetting {

  private String keyStoreFile;
  private String keyStorePass;
  private String keyAlias;
  private String keyPass;
  private int tokenExpiresMin;
  private int appTokenExpiresMin;
}
