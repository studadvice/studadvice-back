package com.stud.studadvice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileDto {
    private String filename;
    private String fileType;
    private String fileSize;
    private byte[] file;
    public FileDto() {
    }
}
