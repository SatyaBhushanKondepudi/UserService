package com.satyabhushan.userservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDto{
    private String token;
    private ResponseStatus responseStatus;
}
