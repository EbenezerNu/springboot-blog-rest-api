package com.springboot.blog.security;

import com.springboot.blog.exception.BlogAPIException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${app.jwt-secret}")
    private String jwtSecretKey;
    @Value("${app.jwt-expiration-milliseconds}")
    private int jwtExpirationDate;


    //Generate token
    public String generateToken(Authentication authentication){
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expiryDate = new Date(currentDate.getTime() + jwtExpirationDate);

        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(currentDate)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecretKey)
                .compact();
        return token;
    }

    // Get username from token;
    public String getUsernameFromJWT(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecretKey)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    //Validate Token

    public boolean validateToken(String token){
        try{
            Jwts.parser()
                    .setSigningKey(jwtSecretKey)
                    .parseClaimsJws(token);
            return true;
        }
        catch(SignatureException ex){
            throw new BlogAPIException("Invalid JWT signature", HttpStatus.BAD_REQUEST);
        }
        catch(MalformedJwtException ex){
            throw new BlogAPIException("Invalid JWT Token", HttpStatus.BAD_REQUEST);
        }
        catch(ExpiredJwtException ex){
            throw new BlogAPIException("Expired JWT Token", HttpStatus.BAD_REQUEST);
        }
        catch(UnsupportedJwtException ex){
            throw new BlogAPIException("Unsupported JWT Token", HttpStatus.BAD_REQUEST);
        }
        catch(IllegalArgumentException ex){
            throw new BlogAPIException("JWT claims string is empty", HttpStatus.BAD_REQUEST);
        }
    }
}
