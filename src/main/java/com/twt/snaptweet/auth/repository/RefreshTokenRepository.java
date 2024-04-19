package com.twt.snaptweet.auth.repository;

import com.twt.snaptweet.auth.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findRefreshTokenByRefreshToken(String refreshToken);
}
