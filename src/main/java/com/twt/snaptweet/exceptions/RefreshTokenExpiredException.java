package com.twt.snaptweet.exceptions;

public class RefreshTokenExpiredException extends RuntimeException{

    public RefreshTokenExpiredException(String message) {
        super(message);
    }
}
