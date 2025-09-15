package com.sky.movieratingservice.interfaces.security;

import com.sky.movieratingservice.domain.User;
import com.sky.movieratingservice.usecases.TokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider implements TokenProvider {

    private final Key signingKey;
    private final Clock clock;
    private final long expirationMinutes;

    public JwtTokenProvider(@Value("${app.jwt.secret}") String secret,
                            @Value("${app.jwt.expiration-minutes:60}") long expirationMinutes,
                            Clock clock) {

        if (secret == null || secret.length() < 32) {
            throw new IllegalArgumentException("JWT secret must be configured and at least 32 characters long");
        }

        this.signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.expirationMinutes = expirationMinutes;
        this.clock = clock;
    }

    @Override
    public String issue(User user, Duration ttl) {
        var now = Instant.now(clock);
        Instant expiry = now.plus(ttl != null ? ttl : Duration.ofMinutes(expirationMinutes));

        var roles = user.getRoles().stream().toList();

        return Jwts.builder()
                .subject(user.getEmail())
                .claim("roles", roles)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiry))
                .signWith(signingKey)
                .compact();
    }

    @Override
    public boolean validate(String token, String subject) {
        String username = extractUsername(token);
        return username.equals(subject) && !isTokenExpired(token);
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}
