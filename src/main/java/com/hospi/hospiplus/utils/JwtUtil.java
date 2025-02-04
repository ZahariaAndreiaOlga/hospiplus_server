package com.hospi.hospiplus.utils;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtil {

    private static final SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();

    public JwtUtil() {
        System.out.println("JWTUtil bean initialized");
    }

    public String generateToken(Integer userId, String email, String role){
        return Jwts.builder()
                .subject(email)
                .claim("role", role)
                .claim("userId", userId)
                .expiration(new Date(System.currentTimeMillis() + 86400000))
                .issuedAt(new Date())
                .id(UUID.randomUUID().toString())
                .signWith(SECRET_KEY)
                .compact();
    }

    public boolean validateToken(String Token){
        try{
            Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(Token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public Claims extractData(String Token){
        return Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(Token).getPayload();
    }

}
