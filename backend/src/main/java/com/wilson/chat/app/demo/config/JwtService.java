package com.wilson.chat.app.demo.config;

import com.wilson.chat.app.demo.entity.User.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    // Inject JWT expiration time and secret key from application properties
    @Value("${jwt.expiration}")
    private long EXPIRATION_MINUTES;

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    // Generate JWT token with extra claims
    public String generateToken(User user, Map<String, Object> extraClaims) {
        Date issuedAt = new Date(System.currentTimeMillis());
        Date expiration = new Date(issuedAt.getTime() + (EXPIRATION_MINUTES * 60 * 1000));
        return Jwts.builder()
                .setClaims(extraClaims) // Set additional claims
                .setSubject(user.getUsername()) // Set the subject (username)
                .setIssuedAt(issuedAt) // Set the issued time
                .setExpiration(expiration) // Set the expiration time
                .signWith(generateKey(), SignatureAlgorithm.HS256) // Sign the token with the secret key and algorithm
                .compact();
    }

    // Generate the signing key from the secret key
    private Key generateKey(){
        byte[] secreateAsBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(secreateAsBytes);
    }

    // Extract the username from the JWT token
    public String extractUsername(String jwt) {
        return extractAllClaims(jwt).getSubject();
    }

    // Extract all claims from the JWT token
    private Claims extractAllClaims(String jwt) {
        return Jwts.parserBuilder().setSigningKey(generateKey()).build()
                .parseClaimsJws(jwt).getBody();
    }

    // Validate token
    public boolean isTokenValid(String token) {
        final String username = extractUsername(token);
        return (username != null && !isTokenExpired(token));
    }

    // Check if the token is expired
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Extract expiration date from token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Extract a specific claim from the token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
}