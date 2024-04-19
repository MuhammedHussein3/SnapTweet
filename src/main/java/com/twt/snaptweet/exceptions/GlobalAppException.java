package com.twt.snaptweet.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalAppException {


    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public Map<String,String> handleUserNotFoundException(final UserNotFoundException exception) {
        Map<String,String> map = new HashMap<>();
        map.put("Error message : ", exception.getMessage());
        return map;
    }

    @ExceptionHandler(RefreshTokenExpiredException.class)
    @ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
    public Map<String,String> handleRefreshTokenExpiredException(final RefreshTokenExpiredException exception) {
        Map<String,String> map = new HashMap<>();
        map.put("Error message : ", exception.getMessage());
        return map;
    }
}
