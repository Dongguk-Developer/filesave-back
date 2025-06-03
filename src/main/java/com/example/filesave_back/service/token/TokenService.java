package com.example.filesave_back.service.token;

import com.example.filesave_back.usecase.token.TokenUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenService implements TokenUseCase {
    public String getNewToken(){
        return UUID.randomUUID().toString();
    }
}
