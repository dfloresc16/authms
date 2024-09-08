package com.pt.authms.config;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.pt.authms.model.entity.Userms;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtProvider {

	
	private static Logger logger = LoggerFactory.getLogger(JwtProvider.class);
    private static final long TOKEN_VALIDITY = 3600 * 1000;

    @Value("${jwt.secret}")
    private String secret;

    private SecretKey getSigningKey() {
        logger.info("Generating signing key from secret.");
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        if (keyBytes.length < 32) {
            throw new IllegalArgumentException("The secret key must be at least 256 bits (32 bytes) long.");
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String createToken(Userms ms) {
        logger.info(String.format("Generating token for user %s with userId %d", ms.getUserName(), ms.getId()));
        Map<String, Object> claims = buildClaims(ms);
        Map<String, Object> headers = buildHeaders(ms);
        Date now = new Date();
        Date exp = new Date(now.getTime() + TOKEN_VALIDITY);

        return Jwts.builder()
                .setHeaderParams(headers)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Map<String, Object> buildClaims(Userms ms) {
        Map<String, Object> claims = Jwts.claims().setSubject(ms.getUserName());
        claims.put("id", ms.getId());
        claims.put("email", ms.getEmail());
        claims.put("name", ms.getUserName());
        return claims;
    }

    private Map<String, Object> buildHeaders(Userms ms) {
        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put("alg", "HS256");
        headers.put("company", "PT");
        return headers;
    }

    public String getUserNameFromToken(String token) {
        try {
            String subject = Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody().getSubject();
            return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody().getSubject();
        } catch (Exception e) {
            logger.info(e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid Token");
        }
    }

    public void validate(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }catch (Exception e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

}
