package com.wtf.cauth.util;

import com.wtf.cauth.data.dto.response.user.AuthTokenResDto;
import com.wtf.cauth.props.JwtProperties;
import com.wtf.cauth.data.dto.response.app.AppDto;
import com.wtf.cauth.data.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Component
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JwtUtil {
    private static final String CAUTH = "cauth";
    private static final String APP_CLAIM = "app";
    private static final String USER_ID_CLAIM = "uid";
    private static final String NAME_CLAIM = "uname";
    private static final String ROLE_CLAIM = "role";
    private final JwtProperties jwtProperties;

    /**
     * @param app
     * @param user
     * @return jwt, expiration time in millis
     */
    public Pair<String, Long> generateAuthToken(AppDto app, User user) {
        long expirationWindow = app.getUserAuthTokenExpiryInMins() * 60 * 1000;
        long current = System.currentTimeMillis();
        long expiry = current + expirationWindow;

        String jwt = Jwts.builder()
            .setIssuer(CAUTH)
            .setSubject(user.getId())
            .claim(APP_CLAIM, app.getName())
            .claim(USER_ID_CLAIM, user.getId())
            .claim(ROLE_CLAIM, user.getRole())
            .setIssuedAt(Date.from(Instant.ofEpochMilli(current)))
            .setExpiration(Date.from(Instant.ofEpochMilli(expiry)))
            .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8))
            .compact();

        return Pair.of(jwt, expiry);
    }

    public AuthTokenResDto validateAuthToken(String jwt) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8))
                .requireIssuer(CAUTH)
                .build()
                .parseClaimsJws(jwt);
            String userId = (String) claims.getBody().get(USER_ID_CLAIM);
            String role = (String) claims.getBody().get(ROLE_CLAIM);
            return new AuthTokenResDto(userId, role, true);
        } catch (JwtException e) {
            log.error("Error on validation jwt token: {}", e.getMessage());
        }
        return new AuthTokenResDto(null, null, false);
    }

}
