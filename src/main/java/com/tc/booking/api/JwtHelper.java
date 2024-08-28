/**
 *
 */
package com.tc.booking.api;

import com.tc.booking.api.exception.ApiException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableEntryException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Date;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author binh
 */
@Component
@Slf4j
public class JwtHelper {
  
  @Autowired
  private JwtSetting jwtConfig;
  
  private KeyStore keystore;
  private PrivateKey privateKey;
  private PublicKey publicKey;

  /**
   *
   * @param subj
   * @param claims
   * @return
   */
  public String genJwtToken(String subj,
      Map<String, Object> claims) {
    return genJwtToken(subj, claims, jwtConfig.getTokenExpiresMin());
  }

  /**
   *
   * @param subj
   * @param claims
   * @param expiresMin
   * @return
   */
  public String genJwtToken(String subj,
      Map<String, Object> claims,
      int expiresMin) {
    Date now = new Date();
    Date expDate = DateUtils.addMinutes(now, expiresMin);
    JwtBuilder bd = Jwts.builder()
        .subject(subj)
        .issuedAt(now)
        .expiration(expDate);
    if (claims != null) {
      claims.forEach((k, v) -> {
        bd.claim(k, v);
      });
    }
    return bd.signWith(privateKey)
        .compact();
    // return bd.encryptWith(privateKey)
    // .compact();
  }

  /**
   *
   * @throws ApiException
   */
  public void loadKeyStore()
      throws ApiException {
    final String keystoreType = "pkcs12";
    final String keystoreFile = jwtConfig.getKeyStoreFile();
    final String keystorePass = jwtConfig.getKeyStorePass();
    final String keyAlias = jwtConfig.getKeyAlias();
    final String keyPass = jwtConfig.getKeyPass();
    try {
      log.info("Loading key store from: {}", keystoreFile);
      keystore = KeyStore.getInstance(keystoreType);
      if (keystoreFile.startsWith("classpath:")) {
        String resPath = keystoreFile.substring("classpath:".length());
        // log.debug("respath: {}", resPath);
        try (InputStream is = this.getClass()
            .getResourceAsStream(resPath)) {
          keystore.load(is, keystorePass.toCharArray());
        }
      } else {
        String filePath = keystoreFile;
        if (filePath.startsWith("file:")) {
          filePath = filePath.substring("file:".length());
        }
        File file = new File(filePath);
        try (InputStream is = new FileInputStream(file)) {
          keystore.load(is, keystorePass.toCharArray());
        }
      }
      Key key = keystore.getKey(keyAlias, keyPass.toCharArray());
      // log.debug("Got key: {}", key);
      if (key instanceof PrivateKey) {
        privateKey = (PrivateKey) key;
        log.info("Got private key, alg={}", privateKey.getAlgorithm());
        
        Certificate cert = keystore.getCertificate(keyAlias);
        publicKey = cert.getPublicKey();
      }
      // log.info("Loaded public key: {}", publicKey);
    } catch (IOException | KeyStoreException | CertificateException
        | NoSuchAlgorithmException | UnrecoverableEntryException ex) {
      log.error("Failed to load keystore: {}", keystoreFile);
      log.error(ex.getMessage(), ex);
    }
  }

  /**
   *
   * @param jwtString
   * @return
   * @throws ApiException
   */
  public Claims parseJwtToken(String jwtString)
      throws ApiException {
    try {
      // Jwt<?, ?> jwt = Jwts.parserBuilder().setSigningKey(privateKey).build().parse(jwtString);
      Jwt<?, ?> jwt = Jwts.parser()
          .verifyWith(publicKey)
          .build()
          .parse(jwtString);
      // log.debug("got jwt: {}", jwt);
      return (Claims) jwt.getPayload();
    } catch (Exception ex) {
      log.error("Failed to parse jwt token: ", jwtString);
      log.error(ex.getMessage(), ex);
      throw new ApiException("Failed to parse jwt");
    }
  }
}
