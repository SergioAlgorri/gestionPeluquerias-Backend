package gestionPeluqueria.security;

public class SecurityConstants {

    public static final String SECRET_KEY = "my_sectret_key";
    public static final int JWT_EXPIRATION_1_HOUR =  3600000;
    public static final String MESSAGE_UNAUTHORIZED = "NO AUTORIZADO: Token inválido o expirado";
}