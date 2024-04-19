package com.twt.snaptweet.auth.service;

import com.twt.snaptweet.auth.model.User;

public interface UserService {

    //1 found user by email
    User findUserByEmail(String email);

    User saveUser(User user);
}
