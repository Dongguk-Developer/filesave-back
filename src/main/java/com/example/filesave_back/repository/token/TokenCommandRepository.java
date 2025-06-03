package com.example.filesave_back.repository.token;

import com.example.filesave_back.entity.token.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenCommandRepository  extends JpaRepository<Token, Long> {
}
