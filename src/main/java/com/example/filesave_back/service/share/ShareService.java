package com.example.filesave_back.service.share;
import com.example.filesave_back.dto.share.ShareDto;
import com.example.filesave_back.entity.token.Token;
import com.example.filesave_back.repository.token.TokenQueryRepository;
import com.example.filesave_back.usecase.share.ShareUseCase;
import com.example.filesave_back.usecase.token.TokenUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShareService implements ShareUseCase {
    private final TokenQueryRepository tokenQueryRepository;

    @Override
    public ResponseEntity<ShareDto.CodeResponse> getCode(String token) {
//        UUID.randomUUID().toString();
        Token temp = tokenQueryRepository.findByData(token).orElse(null);
        if(temp != null){
            throw new RuntimeException("중복 토큰 사용");
        }
        String newToken = UUID.randomUUID().toString();
        return ResponseEntity.ok().body(ShareDto.CodeResponse.builder().code(newToken).build());
    }
}
