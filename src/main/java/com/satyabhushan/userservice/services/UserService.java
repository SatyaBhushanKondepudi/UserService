package com.satyabhushan.userservice.services;

import com.satyabhushan.userservice.Exceptions.TokenInvalidException;
import com.satyabhushan.userservice.Exceptions.UnAuthorizedException;
import com.satyabhushan.userservice.Exceptions.UserNotFoundException;
import com.satyabhushan.userservice.models.Token;
import com.satyabhushan.userservice.models.User;

public interface UserService {
    User signUp(String name, String email, String password);

    Token login(String email, String password) throws UserNotFoundException, UnAuthorizedException;

    User validateToken(String tokenValue);

    void logout(String tokenValue) throws TokenInvalidException;
}
