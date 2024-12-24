package com.ict.finalpj.common.util;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;      // 비밀 키
    
    @Value("${jwt.expiration}")
    private long expiration;    // 만료 시간

    private SecretKey getKey(){
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // 토큰 생성2
    public String generateToken(String idx){
        Map<String, Object> claims = new HashMap<>();
        return generateToken(idx, claims);
    }

    // 토큰 생성
    public String generateToken(String userIdx, Map<String, Object> claims){
        // 내용을 추가하고 싶은 경우
        // 하지만 보안 때문에 중요한 정보를 넣으면 안됨 (보통 아이디, 이름까지만)
        
        // claims.put("email", "hong123@naver.com");

        return Jwts.builder()
        .setClaims(claims)
        .setSubject(userIdx)
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis()+expiration))
        .signWith(getKey(), SignatureAlgorithm.HS256)
        .compact();
    }


    
    // 토큰 유효성 검사
    // UserDetails = 유저 정보를 로드하며 관리하는 역할을 한다
    public Boolean validateToken(String jwtToken, UserDetails userDetails){
        final String username = extractUserName(jwtToken);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(jwtToken));
    }

    // 토큰에서 이름 추출 = jwtToken에서 Subject
    public String extractUserName(String jwtToken){
        return extractClaim(jwtToken, Claims::getSubject);
    }

    // 특정 데이터 추출
    public <T> T extractClaim(String jwtToken, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(jwtToken);
        return claimsResolver.apply(claims);
    }

    // 모든 claim 추출
    private Claims extractAllClaims(String jwtToken){
        return Jwts.parserBuilder()
            .setSigningKey(getKey())
            .build()
            .parseClaimsJws(jwtToken)
            .getBody();
    }

    // 만료 여부 확인
    private Boolean isTokenExpired(String jwtToken){
        return extractExpiration(jwtToken).before(new Date());
    }

    // 만료 시간 추출
    public Date extractExpiration(String jwtToken){
        return extractClaim(jwtToken, Claims::getExpiration);
    }
}
