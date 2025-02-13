package com.satyabhushan.userservice.advices;

import com.satyabhushan.userservice.Exceptions.UserAlreadyExistsException;
import com.satyabhushan.userservice.Exceptions.UserNotFoundException;
import com.satyabhushan.userservice.dtos.ErrorDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<ErrorDto> handleUserNotFoundException(UserNotFoundException e){
        ErrorDto errorDto = new ErrorDto();
        errorDto.setMessage(e.getMessage());
        errorDto.setStatus("404");
        return ResponseEntity.badRequest().body(errorDto);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorDto> handleUserAlreadyExistsException(UserAlreadyExistsException e){
        ErrorDto errorDto = new ErrorDto();
        errorDto.setMessage(e.getMessage());
        errorDto.setStatus("400");
        return ResponseEntity.badRequest().body(errorDto);
    }
}
