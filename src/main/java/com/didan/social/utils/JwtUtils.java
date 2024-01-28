package com.didan.social.utils;

import com.didan.social.entity.BlacklistToken;
import com.didan.social.repository.BlacklistRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtils {
    private final BlacklistRepository blacklistRepository;
    @Autowired
    public JwtUtils(BlacklistRepository blacklistRepository){
        this.blacklistRepository = blacklistRepository;
    }
    @Value("${jwt.secretkey}")
    private String secretKey;
    private static final long ACCESS_TOKEN_EXPIRATION_TIME = 24 * 60 * 60 * 1000; // Thời gian hết hạn của access token (1 ngày)

    // Mã hóa data, email thành accessToken dùng để xác thực người dùng
    public String generateAccessToken(String data){
        Date now = new Date();
        Date exp = new Date(now.getTime() + ACCESS_TOKEN_EXPIRATION_TIME);
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(this.secretKey));
        String accessToken = Jwts.builder().signWith(key).subject(data).issuedAt(now).expiration(exp).compact();
        return accessToken;
    }

    // Giải mã accessToken để lấy userId
    public String getEmailUserFromAccessToken(String accessToken){
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(this.secretKey)); // Giải mã secretKey
        Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(accessToken).getPayload(); // Giải mã accessToken để lấy payload (data) trong accessToken 
        return claims.getSubject().toString(); // Lấy key subject trong payload để trả về email
    }

    // Xác thực accessToken
    public void validateAccessToken(String accessToken) throws Exception {
        try{
            BlacklistToken blacklistToken = blacklistRepository.findFirstByToken(accessToken);
            if(blacklistToken != null) throw new Exception("Invalid access token"); // Nếu token ở trong blacklist thì không xác thực
            SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(this.secretKey));
            Jwts.parser().verifyWith(key).build().parseSignedClaims(accessToken); // Giải mã accessToken
        }catch(MalformedJwtException e){ // Nếu access token không hợp lệ thì bắn lỗi
            throw new Exception("Invalid access token");
        }catch(ExpiredJwtException e) { // Nếu access token hết hạn thì bắn lỗi
            throw new Exception("Expired access token");
        }catch(UnsupportedJwtException e){ // Nếu access token không được hỗ trợ thì bắn lỗi
            throw new Exception("Unsupported access token");
        }catch(IllegalArgumentException e){ // Nếu không có thông tin trong access token thì bắn lỗi
            throw new Exception("Empty access token");
        }
    }
}
