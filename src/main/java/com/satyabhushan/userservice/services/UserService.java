package com.satyabhushan.userservice.services;

import com.satyabhushan.userservice.models.Token;
import com.satyabhushan.userservice.models.User;
import com.satyabhushan.userservice.repositories.TokenRepository;
import com.satyabhushan.userservice.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Service("userService")
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private TokenRepository tokenRepository;

//    public UserService(UserRepository userRepository , BCryptPasswordEncoder bCryptPasswordEncoder) {
//        this.userRepository = userRepository;
//        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
//    }

    public Token login(String email , String password) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isEmpty()){
            throw new RuntimeException("User not found");
        }
        User user = optionalUser.get();
        if(bCryptPasswordEncoder.matches(password , user.getHashedPassword())){
            Token token = createToken(user);
            Token savedToken = tokenRepository.save(token);
            return savedToken;
        }
        return null;
    }

    public User signUp(String name , String email , String password) {
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        //First encrypt the password using Bcrypt alogorithm before saving it to the database
        user.setHashedPassword(bCryptPasswordEncoder.encode(password));
        return userRepository.save(user);
    }

    public void logout(String tokenValue) {
        tokenRepository.deleteByValue(tokenValue);
    }
    public User validateToken(String tokenValue) {
        Optional<Token> optionalToken = tokenRepository.findByValue(tokenValue);
        if(optionalToken.isEmpty()){
            throw new RuntimeException("Invalid token");
        }
        Token token = optionalToken.get();
        if(token.getExpiryDate().before(new Date())){
            throw new RuntimeException("Token expired");
        }
        return token.getUser();
    }


    private Token createToken(User user) {
        Token token = new Token();
        token.setUser(user);
        token.setValue(RandomStringUtils.random(120));
        LocalDate today = LocalDate.now();
        LocalDate thirtyDaysLater = today.plusDays(30);
        Date expiryDate = Date.from(thirtyDaysLater.atStartOfDay(ZoneId.systemDefault()).toInstant());
        token.setExpiryDate(expiryDate);
        return token;
    }
}
