package com.satyabhushan.userservice.controllers;

import com.satyabhushan.userservice.Exceptions.TokenInvalidException;
import com.satyabhushan.userservice.Exceptions.UserAlreadyExistsException;
import com.satyabhushan.userservice.Exceptions.UserNotFoundException;
import com.satyabhushan.userservice.dtos.*;
import com.satyabhushan.userservice.dtos.ResponseStatus;
import com.satyabhushan.userservice.models.Token;
import com.satyabhushan.userservice.models.User;
import com.satyabhushan.userservice.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    // login , signUp, validatetoken , logout

    // login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto requestDto) throws UserNotFoundException {
        LoginResponseDto responseDto = new LoginResponseDto();
        Token token = userService.login(requestDto.getEmail() , requestDto.getPassword());
        responseDto.setToken(token.getValue());
        responseDto.setResponseStatus(ResponseStatus.SUCCESS);
        return new ResponseEntity<>(responseDto , HttpStatus.OK);
    }
    // signup
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequestDto requestDto) throws UserAlreadyExistsException {
        User user = userService.signUp(requestDto.getName() ,
                requestDto.getEmail() ,
                requestDto.getPassword());
        UserDto userDto = UserDto.from(user);
        return new ResponseEntity<>(userDto , HttpStatus.OK);
    }
    // logout
    @PatchMapping("/logout")
    public void logout(@RequestBody LogoutRequestDto requestDto) throws TokenInvalidException {
        userService.logout(requestDto.getToken());
    }

    // validate token
    @GetMapping("/validate")
    public UserDto validateToken(@RequestParam("token") String token) {
        User user = userService.validateToken(token);
        return UserDto.from(user);
    }
}
