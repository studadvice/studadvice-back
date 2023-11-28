package com.stud.studadvice.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class File {
    private String filename;
    private String fileType;
    private String fileSize;
    private byte[] file;
    public File() {
    }
}
