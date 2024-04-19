package com.twt.snaptweet.auth.service.impl;

import com.twt.snaptweet.auth.model.User;
import com.twt.snaptweet.auth.repository.UserRepository;
import com.twt.snaptweet.auth.service.UserService;
import com.twt.snaptweet.exceptions.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;


    @Override
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(()-> new UserNotFoundException(String.format("User Not found by email: %s , Please enter valid email or username",email)));
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }
}
