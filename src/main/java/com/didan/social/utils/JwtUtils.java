package com.didan.social.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtils {
    @Value("${jwt.secretkey}")
    private String secretKey;
    private static final long ACCESS_TOKEN_EXPIRATION_TIME = 900000; // Thời gian hết hạn của access token (15 phút)
    private static final long REFRESH_TOKEN_EXPIRATION_TIME = 2592000000L; // Thời gian hết hạn của refresh token (30 ngày)
    public String generateAccessToken(String data){
        Date now = new Date();
        Date exp = new Date(now.getTime() + ACCESS_TOKEN_EXPIRATION_TIME);
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(this.secretKey));
        String accessToken = Jwts.builder().signWith(key).subject(data).issuedAt(now).expiration(exp).compact();
        return accessToken;
    }
    public String generateRefreshToken(String data){
        Date now = new Date();
        Date exp = new Date(now.getTime() + REFRESH_TOKEN_EXPIRATION_TIME);
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(this.secretKey));
        String refreshToken = Jwts.builder().signWith(key).subject(data).issuedAt(now).expiration(exp).compact();
        return refreshToken;
    }
}
