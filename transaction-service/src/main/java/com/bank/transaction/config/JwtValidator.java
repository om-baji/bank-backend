package com.bank.transaction.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class JwtValidator {

    private final SecretKey key;

    public JwtValidator(@Value("${jwt.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public boolean validateTokenAndUsername(String token,String username) {
        Claims claims =  Jwts
                .parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return (claims.getSubject().contains(username) && claims.getExpiration().after(new Date()));
    }

    public String extractUsername(String token) {
        String username = null;
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            username = claims.getSubject();

        } catch (Exception ex) {
            log.error("Error in username parsing... " + ex.getMessage());
        }

        return username;
    }

    public List<String> extractRoles(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return claims.get("roles", List.class);

        } catch (Exception ex) {
            log.error("Error extracting roles: " + ex.getMessage());
            return Collections.emptyList();
        }
    }

    public String extractId(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return claims.get("userId",String.class);
        } catch (Exception ex) {
            log.error("Error decoding userId from claims : " + ex.getMessage());
            return null;
        }
    }
}
