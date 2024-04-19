package com.twt.snaptweet.auth.service.impl;

import com.twt.snaptweet.auth.model.RefreshToken;
import com.twt.snaptweet.auth.model.User;
import com.twt.snaptweet.auth.repository.RefreshTokenRepository;
import com.twt.snaptweet.auth.service.RefreshTokenService;
import com.twt.snaptweet.exceptions.RefreshTokenExpiredException;
import com.twt.snaptweet.exceptions.RefreshTokenNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserServiceImpl userService;


    public RefreshToken createRefreshToken(String username) {
        User user = userService.findUserByEmail(username);

        RefreshToken rt = user.getRefreshToken();

        if (rt == null) {
            long refreshTokenTimeStamp = 5 * 60 * 10000;
            RefreshToken saveRf = RefreshToken.builder()
                    .refreshToken(UUID.randomUUID().toString())
                    .expirationTime(Instant.now().plusMillis(refreshTokenTimeStamp))
                    .user(user)
                    .build();
            return refreshTokenRepository.save(saveRf);
        }
        return rt;
    }

    public RefreshToken verifyRefreshToken(String refreshToken) {
        RefreshToken rt = refreshTokenRepository.findRefreshTokenByRefreshToken(refreshToken)
                .orElseThrow(() -> new RefreshTokenNotFoundException(String.format("Refresh token %s not found", refreshToken)));

        if (rt.getExpirationTime().isBefore(Instant.now())) {
            refreshTokenRepository.deleteById(rt.getId());
            throw new RefreshTokenExpiredException(String.format("Refresh token %s expired", refreshToken));
        }

        return rt;
    }
}
/**
 * package com.movieflix.auth.service;
 *
 * import com.movieflix.auth.entities.RefreshToken;
 * import com.movieflix.auth.entities.User;
 * import com.movieflix.auth.repositories.RefreshTokenRepository;
 * import com.movieflix.exception.RefreshTokenNotFoundException;
 * import com.movieflix.exception.UserNotFoundException;
 * import lombok.RequiredArgsConstructor;
 * import org.springframework.security.core.userdetails.UsernameNotFoundException;
 * import org.springframework.stereotype.Service;
 * import org.springframework.transaction.annotation.Transactional;
 *
 * import java.time.Instant;
 * import java.util.Optional;
 * import java.util.UUID;
 *
 * @Service
 * @RequiredArgsConstructor
 * public class RefreshTokenService {
 *     private final RefreshTokenRepository refreshTokenRepository;
 *     private final UserServiceImpl userService;
 *
 *     public RefreshToken createRefreshToken(String username) throws UserNotFoundException {
 *         User user =  userService.findUserByEmail(username).orElseThrow(()->new UserNotFoundException("User Not found In DB"));
 *
 *         RefreshToken refreshToken = user.getRefreshToken();
 *
 *         if (refreshToken == null){
 *             long refreshTokenTimestamp = 5 * 60 * 60 * 10000;
 *             refreshToken = RefreshToken.builder().
 *                     refreshToken(UUID.randomUUID().toString()).
 *                     expirationTime(Instant.now().plusMillis(refreshTokenTimestamp)).
 *                     user(user).
 *                     build();
 *
 *             refreshTokenRepository.save(refreshToken);
 *         }
 *         return refreshToken;
 *     }
 *     @Transactional
 *     public RefreshToken verifyRefreshToken(String refreshToken) throws UserNotFoundException, RefreshTokenNotFoundException {
 *       RefreshToken re = refreshTokenRepository.findRefreshTokenByRefreshToken(refreshToken).orElseThrow(()->new
 *                                                                        UserNotFoundException("User Not found In DB"));
 *
 *         if (re.getExpirationTime().compareTo(Instant.now()) < 0){
 *             refreshTokenRepository.delete(re);
 *             throw new RefreshTokenNotFoundException("Refresh Token Expired");
 *         }
 *           return re;
 *     }
 * }
 */
