package com.bank.gateway.helpers;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@Component
public class Helper {

    private SecretKey secretKey;

    public Helper(@Value("${hash.secret}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String hashToken(String userId, String email, List<String> roles) {
        return Jwts
                .builder()
                .subject(userId)
                .signWith(this.secretKey, SignatureAlgorithm.HS256)
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + 15 * 60 * 1000))
                .claim("email",email)
                .claim("roles",roles)
                .compact();
    }
}
