package dev.trabalho.xfragil.security.jwt;

import dev.trabalho.xfragil.entities.Users;
import dev.trabalho.xfragil.exception.customExceptions.ExpiredTokenException;
import dev.trabalho.xfragil.exception.customExceptions.InvalidTokenException;
import dev.trabalho.xfragil.security.UserDetailsImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtil {

    private final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    private Date now() {
        return new Date();
    }

    //Date expirationDate = new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1)); // 1 dia
    Date expirationDate = new Date(System.currentTimeMillis() + 30 * 1000);
    
    public String generateToken(UserDetails userDetails) {

        UserDetailsImpl userDetailsImpl = (UserDetailsImpl) userDetails;
        Users user = userDetailsImpl.getUser();

        return Jwts.builder()
                .setSubject(user.getLogin())
                .claim("role", user.getRole().name())
                .setIssuedAt(now())
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
    
    
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token);
            return !isTokenExpired(token);
        } catch (ExpiredJwtException e) {
            throw new ExpiredTokenException("Token expirado. Faça login novamente.");
        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException e) {
            throw new InvalidTokenException("Token inválido ou assinatura incorreta.");
        }
    }

    private boolean isTokenExpired(String token) {
        Date expiration = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.before(now());
    }

}
