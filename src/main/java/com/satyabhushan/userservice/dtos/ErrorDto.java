package com.satyabhushan.userservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorDto {
    private String message;
    private String status;

    public ErrorDto(String message, String status) {
        this.message = message;
        this.status = status;
    }

    public ErrorDto() {
    }
}
