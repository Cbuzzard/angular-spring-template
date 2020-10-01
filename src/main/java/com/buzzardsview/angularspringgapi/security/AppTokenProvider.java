package com.buzzardsview.angularspringgapi.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@Component
public class AppTokenProvider {

    private static final long EXPIRATION_TIME_SECONDS = 864_000_000; // 10 days
    private static final String SECRET = System.getenv("token_secret");
    private static final String HEADER_STRING = "Authorization";

    public static void addAuthentication(HttpServletResponse res, String username) {
        res.addHeader(HEADER_STRING, getToken(username));
    }

    public static String getToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.nanoTime() + EXPIRATION_TIME_SECONDS))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    public static Optional<String> getUserFromToken(HttpServletRequest request) {
        final String token = request.getHeader(HEADER_STRING);

        if (token != null) {
            try {
                Claims body = Jwts.parser()
                        .setSigningKey(SECRET)
                        .parseClaimsJws(token.replace("", ""))
                        .getBody();
                final Instant expiration = body.getExpiration().toInstant();

                if (expiration.isBefore(Instant.now())) {
                    return Optional.empty();
                }
                return Optional.of(body.getSubject());

            } catch (SignatureException e) {
            }
        }
        return Optional.empty();
    }
}
