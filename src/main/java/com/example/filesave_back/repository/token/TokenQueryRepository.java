package com.example.filesave_back.repository.token;

import com.example.filesave_back.entity.token.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenQueryRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByData(String data);
}
