package com.abujava.springserver.security;

import com.abujava.springserver.config.AppProperties;
import com.abujava.springserver.model.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
public class TokenProvider {


    private final AppProperties appProperties;

    public TokenProvider(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    public String createToken(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + appProperties.getAuth().getTokenExpirationMsec());
        SecretKey secretKey = Keys.hmacShaKeyFor(appProperties.getAuth().getTokenSecret().getBytes());

        return Jwts.builder()
                .setSubject(user.getId().toString())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    public UUID getUserIdFromToken(String token) {
        SecretKey secretKey = Keys.hmacShaKeyFor(appProperties.getAuth().getTokenSecret().getBytes());
        Claims body = (Claims) Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parse(token)
                .getBody();

        return UUID.fromString(body.getSubject());
    }

    public boolean validateToken(String authToken) {
        try {
            SecretKey secretKey = Keys.hmacShaKeyFor(appProperties.getAuth().getTokenSecret().getBytes());
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(authToken);
            return true;
        } catch (SecurityException ex) {
            log.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }
        return false;
    }

}
