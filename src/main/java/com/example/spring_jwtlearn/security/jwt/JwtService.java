package com.example.spring_jwtlearn.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final SecretKey SECRET_KEY = Jwts.SIG.HS256.key()
                                                       .build();

    private final long EXPIRATION_TIME = 1000 * 60 * 15;


    // EXTRACT INFORMATION FROM TOKEN
    public Claims extractAllClaims(String token) {

        return Jwts.parser()
                   .verifyWith(SECRET_KEY)
                   .build()
                   .parseSignedClaims(token)
                   .getPayload();
    }


    public <T> T extractClaim(String token, Function<Claims, T> claimResolver) {

        Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }


    public String extractEmail(String token) {

        return extractClaim(token, Claims::getSubject);
    }


    public List<SimpleGrantedAuthority> extractAuthorities(String token) {

        List<String> authorities = extractAllClaims(token).get("authorities", List.class);
        return authorities.stream()
                          .map(SimpleGrantedAuthority::new)
                          .toList();
    }


    public Date extractExpiration(String token) {

        return extractClaim(token, Claims::getExpiration);
    }

    //TOKEN GENERATION


    public String generateToken(UserDetails userDetails) {

        List<String> Authorities = userDetails.getAuthorities()
                                              .stream()
                                              .map(GrantedAuthority::getAuthority)
                                              .toList();

        return Jwts.builder()
                   .subject(userDetails.getUsername())
                   .claim("authorities", Authorities)
                   .issuedAt(new Date(System.currentTimeMillis()))
                   .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                   .signWith(SECRET_KEY)
                   .compact();
    }


    //TOKEN VALIDATION
    public boolean isValidToken(String token) {

        Claims claims;
        try {

            claims = extractAllClaims(token);

        } catch (Exception e) {
            return false;
        }
        return claims.getExpiration()
                     .after(new Date());
    }

}
