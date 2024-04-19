package com.twt.snaptweet.auth.service;

import com.twt.snaptweet.auth.utilis.AuthResponse;
import com.twt.snaptweet.auth.utilis.LoginRequest;
import com.twt.snaptweet.auth.utilis.RegisterRequest;
import com.twt.snaptweet.exceptions.UserNotFoundException;

public interface AuthService {

      AuthResponse register(RegisterRequest registerRequest) throws UserNotFoundException;

      AuthResponse login(LoginRequest loginRequest) throws UserNotFoundException;

}
