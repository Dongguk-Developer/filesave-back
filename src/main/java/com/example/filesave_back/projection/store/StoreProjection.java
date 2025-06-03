package com.example.filesave_back.projection.store;

import lombok.Builder;

import java.time.LocalDateTime;

public class StoreProjection {
    @Builder
    public record FileSummary(
            String filename,
            Long filesize,
            LocalDateTime createdAt
    ){}
}
