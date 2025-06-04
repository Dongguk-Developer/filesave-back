package com.example.filesave_back.usecase.file;

import com.example.filesave_back.dto.store.StoreDto.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.Resource;

import java.net.MalformedURLException;

public interface FileUseCase {
    ResponseEntity<FileUploadResponse> uploadFiles(String token,FileUploadRequest request, HttpServletResponse response);
    ResponseEntity<FileSummaryResponse> getFiles(String token,String code);
    ResponseEntity<Resource> downloadFile(String filename, String timeStamp)  throws MalformedURLException;
}
