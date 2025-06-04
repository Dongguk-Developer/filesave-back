package com.example.filesave_back.projection.store;

import com.example.filesave_back.entity.store.FileType;
import lombok.Builder;

import java.time.LocalDateTime;

public class StoreProjection {
    @Builder
    public record FileSummary(
            String filename,
            Long filesize,
            FileType fileType,
            LocalDateTime createdAt
    ){}
}
