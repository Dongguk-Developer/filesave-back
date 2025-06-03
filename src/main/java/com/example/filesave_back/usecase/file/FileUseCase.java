package com.example.filesave_back.usecase.file;

import com.example.filesave_back.dto.store.StoreDto.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

public interface FileUseCase {
    ResponseEntity<FileUploadResponse> uploadFiles(String token,FileUploadRequest request, HttpServletResponse response);
    ResponseEntity<FileSummaryResponse> getFiles(String token,String code);
}
