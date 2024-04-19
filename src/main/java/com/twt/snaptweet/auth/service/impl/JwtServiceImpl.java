package com.twt.snaptweet.auth.service.impl;

import com.twt.snaptweet.auth.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {

    private static final String SECRET_KEY = "DFASKL42123KAMQR49FJ8UJRMAJS8U452OKJFSIA4I1KV134KJMKQUIJMVA";

    public String extractUsername(String token) {
        return extractClaim(token , Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignKey(){
        byte[] bytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(bytes);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(),userDetails);
    }
    private String generateToken(Map<String , Object> claims , UserDetails userDetails) {
        return Jwts
                .builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 2000 * 60 * 20))
                .signWith(getSignKey() , SignatureAlgorithm.HS256)
                .compact();
    }
    public boolean isTokenValid(String token , UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !getExpirationDate(token).before(new Date());

    }

    private boolean isTokenExpired(String token) {
        return getExpirationDate(token).before(new Date());
  }

    private Date getExpirationDate(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
