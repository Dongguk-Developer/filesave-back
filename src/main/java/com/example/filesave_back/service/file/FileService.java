package com.example.filesave_back.service.file;
import com.example.filesave_back.dto.store.StoreDto.*;
import com.example.filesave_back.entity.store.FileType;
import com.example.filesave_back.entity.store.Store;
import com.example.filesave_back.entity.share.Share;
import com.example.filesave_back.entity.token.Token;
import com.example.filesave_back.projection.store.StoreProjection.*;
import com.example.filesave_back.repository.share.ShareCommandRepository;
import com.example.filesave_back.repository.share.ShareQueryRepository;
import com.example.filesave_back.repository.store.StoreCommandRepository;
import com.example.filesave_back.repository.store.StoreQueryRepository;
import com.example.filesave_back.repository.token.TokenQueryRepository;
import com.example.filesave_back.service.token.TokenService;
import com.example.filesave_back.usecase.file.FileUseCase;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;
import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.springframework.core.io.Resource;

@Service
@RequiredArgsConstructor
public class FileService implements FileUseCase {
    private final ShareQueryRepository shareQueryRepository;
    private final ShareCommandRepository shareCommandRepository;
    private final StoreCommandRepository storeCommandRepository;
    private final StoreQueryRepository storeQueryRepository;
    @Value("${file.upload-dir}")
    private String uploadDirectory;

    private final TokenService tokenService;

    private final TokenQueryRepository tokenQueryRepository;

//    @PostConstruct
//    public void initPaths() {
//        uploadDirectory = uploadDirectory;
//    }

    @Override
    @Transactional
    public ResponseEntity<FileUploadResponse> uploadFiles(String token,FileUploadRequest request, HttpServletResponse response){
        System.out.println("57");
        Token temp = tokenQueryRepository.findByData(token).orElse(null);
        if(temp != null){
            System.out.println(token);
            throw new RuntimeException("중복 토큰 사용");
        }
        String code = request.code();
        System.out.println("63");

        List<Share> shares = shareQueryRepository.findByCode(code).orElse(new ArrayList<>());
        System.out.println(shares);
        if(!shares.isEmpty()){
//            System.out.println("중복 공유 코드 사용");
//            System.out.println(code);
//            for(Share share : shares){
//                System.out.println(share.getCode());
//                System.out.println(share.getId());
//            }
            throw new RuntimeException("중복 공유 코드 사용");
        }
        System.out.println("70");


        List<MultipartFile> files = request.files();
        String newToken = tokenService.getNewToken();
        List<CompletableFuture<FileSummary>> newFiles = new ArrayList<>();


        int index = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String dateTime = sdf.format(new Date());
        String[] parts = dateTime.split("_");
        String date = parts[0];
        String timeStamp = parts[1];

        System.out.println("85");

        for (MultipartFile file : files) {
            final int fileIndex = index;

            int finalIndex = index;
            CompletableFuture<FileSummary> future = CompletableFuture.supplyAsync(() -> {
                try {
                    File groupDirectory = new File(uploadDirectory);

                    // synchronized - 경쟁 조건 방지
                    synchronized (FileService.class) {
                        if (!groupDirectory.exists() && !groupDirectory.mkdirs()) {
                            throw new IOException("Failed to create group directory: " + groupDirectory.getAbsolutePath());
                        }
                    }

                    String originalFilename = file.getOriginalFilename();
                    String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));

                    String fileName = date + "_" + timeStamp + "_" + finalIndex + fileExtension;

                    GeneratedFileInfo fileInfo =  GeneratedFileInfo.builder()
                            .date(date)
                            .timeStamp(timeStamp)
                            .fileName(fileName)
                            .index(finalIndex)
                            .build();
                    FileType fileType;
                    if(
                            fileExtension.equals("mp4")
                            ||fileExtension.equals("m4a")
                            ||fileExtension.equals("avi")
                            ||fileExtension.equals("flv")
                            ||fileExtension.equals("wmv")
                            ||fileExtension.equals("3gp")
                            ||fileExtension.equals("m4v")
                            ||fileExtension.equals("mkv")
                            ||fileExtension.equals("mpg")
                            ||fileExtension.equals("webm")
                            ||fileExtension.equals("mov")
                    ){
                        fileType = FileType.VIDEO;
                    }
                    else if(fileExtension.equals("png")
                            ||fileExtension.equals("jpeg")
                            ||fileExtension.equals("jpg")
                            ||fileExtension.equals("gif")
                            ||fileExtension.equals("webp")
                            ||fileExtension.equals("bmp")
                    ){
                        fileType = FileType.IMAGE;
                    }
                    else if(fileExtension.equals("mp3")
                        ||fileExtension.equals("wav")
                        ||fileExtension.equals("wma")
                    ){
                        fileType = FileType.AUDIO;
                    }
                    else if(fileExtension.equals("pdf")
                        ||fileExtension.equals("doc")
                        ||fileExtension.equals("docx")
                        ||fileExtension.equals("xlsx")
                        ||fileExtension.equals("xls")
                        ||fileExtension.equals("ppt")
                        ||fileExtension.equals("pptx")
                        ||fileExtension.equals("txt")
                        ||fileExtension.equals("zip")
                        ||fileExtension.equals("rar")
                        ||fileExtension.equals("7z")
                        ||fileExtension.equals("hwp")
                        ||fileExtension.equals("hwpx")
                        ||fileExtension.equals("csv")){
                        fileType = FileType.DOCUMENT;
                    }
                    else{
                        fileType = FileType.OTHER;
                    }
                    Path filePath = Paths.get(groupDirectory.getAbsolutePath(), fileInfo.fileName());
                    file.transferTo(filePath.toFile());
                    Store store = storeCommandRepository.save(Store.builder().filename(fileInfo.fileName()).fileType(fileType).createdAt(LocalDateTime.now()).filesize(file.getSize()).build());
                    shareCommandRepository.save(Share.builder().code(code).store(store).build());
                    return FileSummary.builder().filename(filePath.toString()).filesize(file.getSize()).createdAt(LocalDateTime.now()).build();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            newFiles.add(future);
            index++;
        }
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(
                newFiles.toArray(new CompletableFuture[0])
        );
        List<FileSummary> fileSummaries = allFutures.thenApply(v -> {
            List<FileSummary> fileNames = new ArrayList<>();
            newFiles.forEach(f -> fileNames.add(f.join()));
            return fileNames;
        }).join();
        ResponseCookie cookie = ResponseCookie.from("token", newToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(60 * 60)
                .sameSite("Strict")
                .build();
        return ResponseEntity.ok()
                .header("Set-Cookie", cookie.toString())
                .body(FileUploadResponse.builder().token(newToken).files(fileSummaries).code(code).build());
    }
    @Override
    public ResponseEntity<FileSummaryResponse> getFiles(String token,String code) {
        Token temp = tokenQueryRepository.findByData(token).orElse(null);
        if (temp != null) {
            throw new RuntimeException("중복 토큰 사용");
        }
        List<Share> files = shareQueryRepository.findByCode(code).orElse(null);
        if (files == null || files.isEmpty()){
            throw new RuntimeException("Not found code");
        }
        String newToken = tokenService.getNewToken();
        ResponseCookie cookie = ResponseCookie.from("token", newToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(60 * 60)
                .sameSite("Strict")
                .build();
        List<FileSummary> filesummaries = new ArrayList<>();
        for (Share share : files) {
            filesummaries.add(FileSummary.builder().filename(share.getStore().getFilename()).filesize(share.getStore().getFilesize()).createdAt(share.getStore().getCreatedAt()).build());
        }
        return ResponseEntity.ok()
                .header("Set-Cookie", cookie.toString())
                        .body(
                                FileSummaryResponse.builder().files(filesummaries).code(code).token(newToken).build()
                        );

    }
    @Override
    public ResponseEntity<Resource> downloadFile(String filename,String timeStamp) throws MalformedURLException {
        LocalDateTime localDateTime = LocalDateTime.parse(timeStamp);
        Store store = storeQueryRepository.findByFilenameAndCreatedAt(filename,localDateTime).orElse(null);

        if(store == null) {
            System.out.println(238);
            System.out.println(filename);
            System.out.println(localDateTime);
            return ResponseEntity.notFound().build();
        }

        Path filePath = Paths.get(uploadDirectory).resolve(filename).normalize();
        Resource resource = new UrlResource(filePath.toUri());



        if (!resource.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        String contentDisposition = "attachment; filename=\"" + resource.getFilename() + "\"";

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }
}
