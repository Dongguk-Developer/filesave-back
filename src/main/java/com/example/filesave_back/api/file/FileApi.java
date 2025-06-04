package com.example.filesave_back.api.file;

import com.example.filesave_back.dto.share.ShareDto.CodeResponse;
import com.example.filesave_back.dto.store.StoreDto;
import com.example.filesave_back.dto.store.StoreDto.*;
import com.example.filesave_back.service.file.FileService;
import com.example.filesave_back.service.share.ShareService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import org.springframework.core.io.Resource;
import java.net.MalformedURLException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
public class FileApi {
    private final FileService fileService;
    private final ShareService shareService;

    @PostMapping("/upload")
    public ResponseEntity<FileUploadResponse> uploadFile(
            @CookieValue(name = "token", required = false, defaultValue = "") String token,
            @ModelAttribute FileUploadRequest request,
            HttpServletResponse response
    ){
        return fileService.uploadFiles(token,request, response);
    }

    @GetMapping("/code")
    public ResponseEntity<CodeResponse> getAccessCode(
            @CookieValue(name = "token", required = false, defaultValue = "") String token){
        return shareService.getCode(token);
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<FileSummaryResponse> getStoreByCode(
            @CookieValue(name = "token", required = false, defaultValue = "") String token,
            @PathVariable String code
    ){
        return fileService.getFiles(token,code);
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam String filename,@RequestParam String timestamp) {
        try {
            return fileService.downloadFile(filename,timestamp);
        } catch (MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
