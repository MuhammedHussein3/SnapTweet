package com.twt.snaptweet.auth.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "refreshTokens")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String refreshToken;

    @Column(nullable = false)
    private Instant expirationTime;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
