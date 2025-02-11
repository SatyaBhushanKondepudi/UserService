package com.satyabhushan.userservice.Exceptions;

public class TokenInvalidException extends Exception{

    public TokenInvalidException(String invalidToken) {
        super(invalidToken);
    }
}
