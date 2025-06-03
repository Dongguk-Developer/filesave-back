package com.example.filesave_back.usecase.share;

import com.example.filesave_back.dto.share.ShareDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;

public interface ShareUseCase {
    ResponseEntity<ShareDto.CodeResponse> getCode(String token);
}
