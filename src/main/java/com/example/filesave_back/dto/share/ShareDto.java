package com.example.filesave_back.dto.share;

import lombok.Builder;

public class ShareDto {
    @Builder
    public record CodeResponse(String code){}
}
