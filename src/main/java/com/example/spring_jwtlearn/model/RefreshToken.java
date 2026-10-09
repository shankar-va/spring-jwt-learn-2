package com.example.spring_jwtlearn.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "refresh-token")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token_hash", nullable = false)
    private String tokenHash;

    @Column(name = "expires_At", nullable = false)
    private Instant expiryDate;

    @ManyToOne
    @JoinColumn(name = "token_id", referencedColumnName = "user_id")
    private User user;

}
