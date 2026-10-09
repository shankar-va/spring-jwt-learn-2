package com.example.spring_jwtlearn.security.jwt.refreshToken;

import com.example.spring_jwtlearn.model.RefreshToken;
import com.example.spring_jwtlearn.model.User;
import com.example.spring_jwtlearn.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository repository;


    public String createRefreshToken(User user) {

        String rawToken = UUID.randomUUID()
                              .toString();
        String hashedToken = hashToken(rawToken);
        RefreshToken refreshToken = RefreshToken.builder()
                                                .user(user)
                                                .tokenHash(hashedToken)
                                                .expiryDate(Instant.now()
                                                                   .plusMillis(1000 * 60 * 60 * 24 * 7))
                                                .build();

        repository.save(refreshToken);
        return rawToken;
    }


    public RefreshToken verifyByRawToken(String rawToken) {

        String hashedToken = hashToken(rawToken);
        RefreshToken refreshToken = repository.findByTokenHash(hashedToken)
                                              .orElseThrow(() -> new RuntimeException("Invalid refresh token"));
        if (refreshToken.getExpiryDate()
                        .compareTo(Instant.now()) < 0) {
            repository.delete(refreshToken);
            throw new RuntimeException("Refresh Token Expired");
        }
        return refreshToken;
    }


    public String hashToken(String rawToken) {

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(rawToken.getBytes());
            return Base64.getEncoder()
                         .encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}
