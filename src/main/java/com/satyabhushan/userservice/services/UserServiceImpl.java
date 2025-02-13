package com.satyabhushan.userservice.services;

import com.satyabhushan.userservice.Exceptions.UnAuthorizedException;
import com.satyabhushan.userservice.Exceptions.UserNotFoundException;
import com.satyabhushan.userservice.models.Token;
import com.satyabhushan.userservice.models.User;
import com.satyabhushan.userservice.repositories.TokenRepository;
import com.satyabhushan.userservice.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{

    private UserRepository userRepository ;
    private BCryptPasswordEncoder passwordEncoder;
    private TokenRepository tokenRepository;


    @Override
    public User signUp(String name, String email, String password) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            // redirect to login
            return optionalUser.get();
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setHashedPassword(passwordEncoder.encode(password));
        user.setRoles(new ArrayList<>());

        //Before returning the object, we should push an sendEmail event
        // to Kafka so that EmailService can read the event and send and Email.


        user = userRepository.save(user);
        return user;
    }

    @Override
    public Token login(String email, String password) throws UserNotFoundException, UnAuthorizedException {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isEmpty()){
            throw new UserNotFoundException("User with Email - " + email + "is not found");
        }
        User user = optionalUser.get();
        if(passwordEncoder.matches(password,user.getHashedPassword())){
            //login successful, create the token.
            Token token = new Token();
            token.setUser(user);
            token.setValue(RandomStringUtils.randomAlphanumeric(120));

            Date currentDate = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentDate);

            // Add 30 days to the calendar
            calendar.add(Calendar.DAY_OF_MONTH, 30);

            // Get the updated time as a Date object
            Date dateAfter30Days = calendar.getTime();

            token.setExpiryAt(dateAfter30Days);

            return tokenRepository.save(token);
        }

        //Login failed.
        throw new UnAuthorizedException("Login failed");

    }

    @Override
    public User validateToken(String tokenValue) {
        //Check if the token is present in the DB, token is NOT deleted and
        //token's expiry time is greater than the current time.
        Optional<Token> optionalToken = tokenRepository.findByValueAndDeletedAndExpiryAtGreaterThan(
                tokenValue, false, new Date()
        );
        //Token invalid
        return optionalToken.map(Token::getUser).orElse(null);
    }

    @Override
    public void logout(String tokenValue) {
        Optional<Token> optionalToken = tokenRepository.findByValue(tokenValue);

        if (optionalToken.isEmpty()) {
            throw new RuntimeException("Token Invalid.");
        }

        Token token = optionalToken.get();
        token.setDeleted(true);
        tokenRepository.save(token);
    }
}
