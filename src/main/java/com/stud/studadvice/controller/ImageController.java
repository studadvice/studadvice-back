package com.stud.studadvice.controller;

import com.stud.studadvice.dto.FileDownloadResponse;
import com.stud.studadvice.dto.FileDto;
import com.stud.studadvice.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@RestController
public class ImageController {

    @Autowired
    private ImageService imageService;

    @GetMapping("/download/{imageId}")
    public ResponseEntity<FileDownloadResponse> download(@PathVariable String imageId) {
        try {
            FileDto file = imageService.download(imageId);
            if (imageService.checkFile(file)) {
                FileDownloadResponse fileDownloadResponse = new FileDownloadResponse();
                fileDownloadResponse.setFilename(file.getFilename());
                fileDownloadResponse.setFileContent(file.getFile());
                fileDownloadResponse.setSize(file.getFileSize());
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fileDownloadResponse);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException ioException) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ioException.getMessage(), ioException);
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("imageFile") MultipartFile imageFile) {
        try {
            String imageId = imageService.upload(imageFile);
            return ResponseEntity.ok().body(imageId);
        } catch (IOException ioException) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to upload the file", ioException);
        }
    }
}
