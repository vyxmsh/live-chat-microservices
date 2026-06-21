package com.slackclone.api_gateway.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.function.Function;

@Component
public class JwtUtil {
    
    @Value("${jwt.secret}")
    private String secret;

    private Key getSigningKey(){
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public boolean validateToken(String token)
    {
        try{
            Jwts.parser()
            .verifyWith((javax.crypto.SecretKey) getSigningKey())
            .build()
            .parseSignedClaims(token);
            
            System.out.println("Token Valid");
            return true;
        }
        catch(JwtException | IllegalArgumentException e)
        {
            System.out.println("TOKEN ERROR: " + e.getClass().getName());
            System.out.println("TOKEN ERROR MSG: " + e.getMessage());
            return false;
        }
    }
    
    public String extractUsername(String token)
    {
        return extractClaim(token, Claims::getSubject);
    } 

    public String extractUserId(String token)
    {
        Object userId = extractClaim(token, claims-> claims.get("userId"));
        return userId != null? userId.toString() : null;    
    }

    private <T> T extractClaim(String token, Function<Claims,T> resolver)
    {
        Claims claims = 
            Jwts.parser()
                .verifyWith((javax.crypto.SecretKey) getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return resolver.apply(claims);
        
    }
}
