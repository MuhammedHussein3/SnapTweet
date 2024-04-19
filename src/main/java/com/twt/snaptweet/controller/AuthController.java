package com.twt.snaptweet.controller;

import com.twt.snaptweet.auth.service.AuthService;
import com.twt.snaptweet.auth.service.impl.AuthServiceIml;
import com.twt.snaptweet.auth.service.impl.JwtServiceImpl;
import com.twt.snaptweet.auth.service.impl.RefreshTokenServiceImpl;
import com.twt.snaptweet.auth.utilis.AuthResponse;
import com.twt.snaptweet.auth.utilis.LoginRequest;
import com.twt.snaptweet.auth.utilis.Process;
import com.twt.snaptweet.auth.utilis.RefreshTokenRequest;
import com.twt.snaptweet.auth.utilis.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/auth/")
@RequiredArgsConstructor
public class AuthController {

    private final AuthServiceIml authService;
    private final RefreshTokenServiceImpl refreshTokenService;
    private final JwtServiceImpl jwtService;

    @PostMapping("register/")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest registerRequest){

        return ResponseEntity.ok(authService.register(registerRequest));
    }

    @PostMapping("login/")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("refresh/")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest){
        var refreshToke = refreshTokenService.verifyRefreshToken(refreshTokenRequest.getRefreshToken());

        var user = refreshToke.getUser();

        var accessToken = jwtService.generateToken(user);

        return ResponseEntity.ok(
                AuthResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshTokenRequest.getRefreshToken())
                        .process(Process.SUCCESSFULLY_REFRESH_TOKEN)
                        .build()
        );
    }
}
