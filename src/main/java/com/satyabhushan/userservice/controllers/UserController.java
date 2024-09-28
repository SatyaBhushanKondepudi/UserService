package com.satyabhushan.userservice.controllers;

import com.satyabhushan.userservice.dtos.*;
import com.satyabhushan.userservice.dtos.ResponseStatus;
import com.satyabhushan.userservice.models.Token;
import com.satyabhushan.userservice.models.User;
import com.satyabhushan.userservice.repositories.UserRepository;
import com.satyabhushan.userservice.services.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    // login , signUpp , validatetoken , logout

    // login
    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody LoginRequestDto requestDto) {
        LoginResponseDto responseDto = new LoginResponseDto();

        try{
            Token token = userService.login(requestDto.getEmail() , requestDto.getPassword());
            responseDto.setToken(token.getValue());
            responseDto.setResponseStatus(ResponseStatus.SUCCESS);
        }
        catch (Exception e){
            responseDto.setResponseStatus(ResponseStatus.FAILURE);
        }

        return responseDto;
    }
    // signup
    @PostMapping("/signup")
    public UserDto signUp(@RequestBody SignUpRequestDto requestDto) {
        User user = userService.signUp(requestDto.getName() ,
                requestDto.getEmail() ,
                requestDto.getPassword());
        return UserDto.from(user);
    }
    // logout
    @PatchMapping("/logout")
    public void logout(@RequestBody LogoutRequestDto requestDto) {
        userService.logout(requestDto.getToken());
    }

    // validate token
    @GetMapping("/validate")
    public UserDto validateToken(String token) {
        User user = userService.validateToken(token);
        return UserDto.from(user);
    }


}
