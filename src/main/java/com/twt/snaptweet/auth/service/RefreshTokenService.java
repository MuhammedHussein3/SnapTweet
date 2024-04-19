package com.twt.snaptweet.auth.service;

import com.twt.snaptweet.auth.model.RefreshToken;
import com.twt.snaptweet.auth.model.User;
import com.twt.snaptweet.exceptions.RefreshTokenExpiredException;
import com.twt.snaptweet.exceptions.RefreshTokenNotFoundException;

import java.time.Instant;
import java.util.UUID;

public interface RefreshTokenService {

     RefreshToken createRefreshToken(String username) ;

     RefreshToken verifyRefreshToken(String refreshToken);
}
