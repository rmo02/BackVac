package com.zup.desafio.controledevacinasapp.service;

import com.zup.desafio.controledevacinasapp.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Service
public class TokenService {
    @Value("${api.security.token.secret}")
    private String secret;

    private Key key;

    @PostConstruct
    public void init() {
        // Inicialize a chave durante a inicialização do serviço
        this.key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    public String generateToken(User user) {
        try {
            Instant expirationTime = LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
            Date expirationDate = Date.from(expirationTime);

            return Jwts.builder()
                    .setIssuer("Projeto Vacina Já")
                    .setSubject(user.getUsername())
                    .setExpiration(expirationDate)
                    .signWith(key)
                    .compact();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar o token", e);
        }
    }

    public String getSubject(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
            return claims.getSubject();
        } catch (Exception e) {
            throw new RuntimeException("Token JWT inválido ou expirado", e);
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}