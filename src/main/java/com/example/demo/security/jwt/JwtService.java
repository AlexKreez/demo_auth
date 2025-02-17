package com.example.demo.security.jwt;

import com.example.demo.domain.entityUser.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationMs;

//Кодирует ключ
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
//Извлекает логин пользователя из токена
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claimsResolver.apply(claims);
    }
//Создает JWT-токены
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMs)) // токен хранится 1 день
                .signWith(getSigningKey())
                .compact();
    }
//Проверяет, валиден ли токен
    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            String username = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();

            return username.equals(userDetails.getUsername());
        } catch (SignatureException e) {
            System.out.println("❌ Ошибка подписи токена: " + e.getMessage());
            return false;
        } catch (JwtException e) {
            System.out.println("❌ Ошибка валидации токена: " + e.getMessage());
            return false;
        }
    }

    public String findExistingTokenForUser(User user) {
        // Реализовать логику поиска токена (например, хранить токены в БД или кэше)
        return null; // Пока заглушка, чтобы всегда выдавать новый токен
    }


    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }
}
