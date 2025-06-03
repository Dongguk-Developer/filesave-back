package com.example.filesave_back.dto.store;

import com.example.filesave_back.projection.store.StoreProjection.*;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

public class StoreDto {
    @Builder
    public record FileUploadRequest(
            String code,
            List<MultipartFile> files
    ) {}
    @Builder
    public record FileUploadResponse(
        String token,
        List<FileSummary> files,
        String code
    ) {}
    @Builder
    public record FileSummaryResponse(
            String token,
            List<FileSummary> files,
            String code
    ){}
    @Builder
    public record GeneratedFileInfo(
        String date,
        String timeStamp,
        Integer index,
        String fileName
    ){}
}
