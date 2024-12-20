package gestionPeluqueria.security;

import gestionPeluqueria.entities.Inheritance.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    public static final String SECRET_KEY = "my_secret_key";

    public String generateToken(User user) {
        Date date = new Date();
        Date expirationDate = new Date(date.getTime() + 300000);

        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("role", user.getRole())
                .setIssuedAt(date)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public String extractUserEmail(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean isTokenValid(String token, CustomUserDetails user) {
        // El getUsername de UserDetails se corresponde con el correo electronico
        return extractUserEmail(token).equals(user.getUsername());
    }
}
