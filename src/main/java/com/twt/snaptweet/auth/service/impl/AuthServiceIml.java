package com.twt.snaptweet.auth.service.impl;

import com.twt.snaptweet.auth.model.Role;
import com.twt.snaptweet.auth.model.User;
import com.twt.snaptweet.auth.service.AuthService;
import com.twt.snaptweet.auth.utilis.AuthResponse;
import com.twt.snaptweet.auth.utilis.LoginRequest;
import com.twt.snaptweet.auth.utilis.Process;
import com.twt.snaptweet.auth.utilis.RegisterRequest;
import com.twt.snaptweet.exceptions.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AuthServiceIml implements AuthService {

    private final UserServiceImpl userService;
    private final JwtServiceImpl jwtService;
    private final RefreshTokenServiceImpl refreshTokenService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    @Override
    public AuthResponse register(RegisterRequest registerRequest) throws UserNotFoundException {

        User user = User.builder()
                .name(registerRequest.getName())
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .createdAt(LocalDate.now())
                .role(Role.USER)
                .build();

        User savedUser =  userService.saveUser(user);

        var accessToken = jwtService.generateToken(savedUser);

        var refreshToken = refreshTokenService.createRefreshToken(savedUser.getEmail());

     return AuthResponse.builder().
             accessToken(accessToken)
                     .refreshToken(refreshToken.getRefreshToken())
                             .process(Process.SUCCESSFULLY_REGISTER)
                                     .build();
    }

    @Override
    public AuthResponse login(LoginRequest loginRequest) throws UserNotFoundException {



       Authentication authentication =  authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        var user = userService.findUserByEmail(loginRequest.getEmail());

        var accessToken = jwtService.generateToken(user);

        var refreshToken = refreshTokenService.verifyRefreshToken(user.getRefreshToken().getRefreshToken());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getRefreshToken())
                .process(Process.SUCCESSFULLY_LOGIN)
                .build();
    }
}

/**
 * @RequiredArgsConstructor
 * public class AuthService {
 *
 *     private final PasswordEncoder passwordEncoder;
 *
 *     private final UserRepository userRepository;
 *
 *     private final JwtServiceImpl jwtService;
 *
 *     private final RefreshTokenService refreshTokenService;
 *
 *     private final AuthenticationManager authenticationManager;
 *     public AuthResponse register(RegisterRequest registerRequest) throws UserNotFoundException {
 *         var user = User.builder()
 *                 .name(registerRequest.getName())
 *                 .email(registerRequest.getEmail())
 *                 .username(registerRequest.getUsername())
 *                 .password(passwordEncoder.encode(registerRequest.getPassword()))
 *                 .role(UserRole.USER)
 *                 .build();
 *
 *         User savedUser = userRepository.save(user);
 *
 *         var accessToken = jwtService.generateToken(savedUser);
 *
 *         var refreshToke = refreshTokenService.createRefreshToken(savedUser.getUsername());
 *
 *         return AuthResponse.builder()
 *                         .accessToken(accessToken)
 *                         .refreshToken(refreshToke.getRefreshToken()).
 *                 build();
 *     }
 *
 *     public AuthResponse login(LoginRequest loginRequest) throws UserNotFoundException {
 *
 *         authenticationManager.authenticate(
 *                 new UsernamePasswordAuthenticationToken(
 *                         loginRequest.getEmail(),
 *                         loginRequest.getPassword()
 *                 )
 *         );
 *
 *         var user = userRepository.findByEmail(loginRequest.getEmail())
 *                 .orElseThrow(()-> new UserNotFoundException(String.format("User not found with email : %s ,Please enter valid email!",loginRequest.getEmail())));
 *
 *         var accessToken = jwtService.generateToken(user);
 *         var refreshToken = refreshTokenService.createRefreshToken(loginRequest.getEmail());
 *
 *         return AuthResponse
 *                 .builder()
 *                 .accessToken(accessToken)
 *                 .refreshToken(refreshToken.getRefreshToken())
 *                 .build();
 *     }
 *
 * }
 */
