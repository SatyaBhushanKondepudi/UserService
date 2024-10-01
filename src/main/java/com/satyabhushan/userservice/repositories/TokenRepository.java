package com.satyabhushan.userservice.repositories;


import com.satyabhushan.userservice.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    void deleteByValue(String tokenValue);

    Optional<Token> findByValueAndDeletedAndExpiryAtGreaterThan(String tokenValue,
                                                                boolean deleted,
                                                                Date currentTime);
}
