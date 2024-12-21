package gestionPeluqueria.security;

import gestionPeluqueria.entities.Inheritance.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    public String generateToken(User user) {
        Date date = new Date();
        // Tiempo de expiración de 1 hora
        Date expirationDate = new Date(date.getTime() + SecurityConstants.JWT_EXPIRATION_1_HOUR);

        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("role", user.getRole())
                .setIssuedAt(date)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, SecurityConstants.SECRET_KEY)
                .compact();
    }

    public String extractUserEmail(String token) {
        return Jwts.parser().setSigningKey(SecurityConstants.SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean isTokenValid(String token, CustomUserDetails user) {
        // El getUsername de UserDetails se corresponde con el correo electronico
        return extractUserEmail(token).equals(user.getUsername());
    }
}