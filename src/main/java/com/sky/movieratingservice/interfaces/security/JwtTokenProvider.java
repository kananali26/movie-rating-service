package com.sky.movieratingservice.interfaces.security;

import com.sky.movieratingservice.domain.User;
import com.sky.movieratingservice.usecases.TokenProvider;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider implements TokenProvider {

//    @Value("${security.jwt.secret-base64}")
    private String secretB64 = "secretasdasdasdasecretasdasdasdasecretasdasdasdasecretasdasdasdasecretasdasdasdasecretasdasdasdasecretasdasdasda";

//    @Value("${security.jwt.ttl-seconds:900}")
    private long ttl = 900;

    @Override
    public String issue(User user, Duration ttl) {
        var now = Instant.now();
        var roles = user.getRoles().stream().toList();
        long exp = ttl != null ? ttl.toSeconds() : 1;
        return Jwts.builder()
                .subject(user.getEmail())
                .claim("roles", roles)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(exp)))
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretB64)))
                .compact();
    }

}
