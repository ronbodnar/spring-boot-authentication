package com.ronbodnar.auth.security.service;

import java.security.Key;
import java.util.Date;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import com.ronbodnar.auth.security.model.UserPrincipal;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

/**
 * Service for managing JSON Web Tokens (JWT) for authentication.
 */
@Component
public class JwtService {

    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);
    private static final String COOKIE_NAME = "auth";

    @Value("${com.ronbodnar.auth.security.jwt.secret}")
    private String secret;

    @Value("${com.ronbodnar.auth.security.jwt.expiration}")
    private long expirationTime;

    /**
     * Retrieves the JWT from cookies in the HTTP request.
     *
     * @param request the HTTP request containing the cookies
     * @return the JWT as a string, or null if no cookie is found
     */
    public String getFromCookies(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, COOKIE_NAME);
        return (cookie != null) ? cookie.getValue() : null;
    }

    /**
     * Builds an empty cookie to clear the authentication token.
     *
     * @return a ResponseCookie representing the cleared authentication token
     */
    public ResponseCookie buildEmptyCookie() {
        return ResponseCookie.from(COOKIE_NAME, "")
                .path("/")
                .maxAge(0)
                .httpOnly(true)
                .build();
    }

    /**
     * Builds a cookie containing a JWT for the specified user.
     *
     * @param userPrincipal the user details for whom the JWT is created
     * @return a ResponseCookie containing the authentication token
     */
    public ResponseCookie buildCookie(UserPrincipal userPrincipal) {
        String jwt = buildTokenFromUsername(userPrincipal.getUsername());
        return ResponseCookie.from(COOKIE_NAME, jwt)
                .path("/")
                .maxAge(24 * 60 * 60) // 1 day
                .httpOnly(true)
                .build();
    }

    /**
     * Retrieves the username from the specified JWT.
     *
     * @param token the JWT from which to extract the username
     * @return the username contained in the JWT
     * @throws JwtException if the token is invalid
     */
    public String getUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Validates the given JWT.
     *
     * @param authToken the JWT to validate
     * @return true if the token is valid, false otherwise
     */
    public boolean validate(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(getKey()).build().parse(authToken);
            return true;
        } catch (JwtException e) {
            logger.error("JWT validation error: {}", e.getMessage());
        }
        return false;
    }

    /**
     * Builds a JWT from the specified username.
     *
     * @param username the username to include in the JWT
     * @return the generated JWT as a string
     */
    public String buildTokenFromUsername(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Retrieves the signing key for JWT.
     *
     * @return the signing key
     */
    private Key getKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }
}
