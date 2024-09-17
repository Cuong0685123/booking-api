package com.tc.booking.api;

import com.tc.booking.api.exception.ApiException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtHelper {

    @Autowired
    private JwtSetting jwtConfig;

    private SecretKey signingKey;

    // Method to generate JWT token
    public String genJwtToken(String subj, Map<String, Object> claims) {
        return genJwtToken(subj, claims, jwtConfig.getTokenExpiresMin());
    }

    // Generate token with expiry
    public String genJwtToken(String subj, Map<String, Object> claims, int expiresMin) {
        Date now = new Date();
        Date expDate = new Date(now.getTime() + expiresMin * 60000L); // Convert minutes to milliseconds

        return Jwts.builder()
            .setSubject(subj)
            .setIssuedAt(now)
            .setExpiration(expDate)
            .addClaims(claims)
            .signWith(SignatureAlgorithm.HS256, signingKey.getEncoded())
            .compact();
    }

    public void loadKeyStore() throws ApiException {
        final String key = jwtConfig.getKeyPass(); // Đọc giá trị từ cấu hình
        signingKey = new SecretKeySpec(key.getBytes(), SignatureAlgorithm.HS256.getJcaName());
    }

    // Parse token and extract claims
    public Claims parseJwtToken(String jwtString) throws ApiException {
        try {
            return Jwts.parser()
                .setSigningKey(signingKey)
                .parseClaimsJws(jwtString)
                .getBody();
        } catch (SignatureException | IllegalArgumentException e) {
            log.error("Failed to parse jwt token: ", jwtString);
            log.error(e.getMessage(), e);
            throw new ApiException("Failed to parse jwt");
        }
    }
}
