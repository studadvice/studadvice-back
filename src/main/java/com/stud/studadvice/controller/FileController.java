package com.stud.studadvice.controller;

import com.stud.studadvice.dto.FileDto;
import com.stud.studadvice.service.FileService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@RestController
public class FileController {
    @Autowired
    private FileService imageService;

    @GetMapping("/download/{imageId}")
    public ResponseEntity<ByteArrayResource> download(@PathVariable String imageId){
        try {
            FileDto file = imageService.download(imageId);
            if (imageService.checkFile(file)) {
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(file.getFileType()))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                        .body(new ByteArrayResource(file.getFile()));
            } else {
                return null;
            }
        }
        catch (IOException ioException){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ioException.getMessage(), ioException);
        }
    }
}
