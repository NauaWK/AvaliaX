package dev.trabalho.xfragil.security.jwt;

import dev.trabalho.xfragil.entities.Users;
import dev.trabalho.xfragil.security.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final Key SECRET_KEY = Keys.hmacShaKeyFor("ChaveDeTeste".getBytes());

    Date now = new Date();
    Date expirationDate = new Date(System.currentTimeMillis() + 1000 * 60 * 60); // 1 hora

    public String generateToken(UserDetails userDetails) {

        UserDetailsImpl userDetailsImpl = (UserDetailsImpl) userDetails;
        Users user = userDetailsImpl.getUser();

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("role", user.getRole().name())
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        UserDetailsImpl userDetailsImpl = (UserDetailsImpl) userDetails;
        String username = extractUsername(token);
        return username.equals(userDetailsImpl.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        Date expiration = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.before(now);
    }

}
