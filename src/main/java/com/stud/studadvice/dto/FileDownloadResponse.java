package com.stud.studadvice.dto;

import lombok.Getter;

import java.util.Base64;

@Getter
public class FileDownloadResponse {
    private String filename;
    private String fileContent;
    private String size;
    public void setFileContent(byte[] fileContent) {
        this.fileContent = Base64.getEncoder().encodeToString(fileContent);
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
