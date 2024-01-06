package com.stud.studadvice.service;

import com.mongodb.client.gridfs.model.GridFSFile;


import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import com.stud.studadvice.dto.FileDto;
import org.apache.commons.io.IOUtils;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@Service
public class ImageService {
    @Autowired
    private GridFsOperations operations;

    @Autowired
    private GridFsTemplate template;
    public FileDto download(String id) throws IOException {

        GridFSFile gridFSFile = template.findOne(new Query(Criteria.where("_id").is(id)));

        FileDto loadFile = new FileDto();

        if (gridFSFile != null && gridFSFile.getMetadata() != null) {
            loadFile.setFilename(gridFSFile.getFilename());
            loadFile.setFile(IOUtils.toByteArray(operations.getResource(gridFSFile).getInputStream()));

            Document metaData = (Document) gridFSFile.getMetadata().get("metadata");

            if (metaData != null) {

                if (metaData.containsKey("contentType")) {
                    loadFile.setFileType(metaData.get("contentType").toString());
                }

                if (metaData.containsKey("contentSize")) {
                    loadFile.setFileSize(metaData.get("contentSize").toString());
                }
            }
        }

        return loadFile;
    }

    public String upload(MultipartFile image) throws IOException {
        if (image.isEmpty()) {
            throw new IllegalArgumentException("Image is empty");
        }
        GridFSUploadOptions options = new GridFSUploadOptions()
                .metadata(new Document("contentType", image.getContentType())
                        .append("contentSize", image.getSize()));
        return template.store(image.getInputStream(), Objects.requireNonNull(image.getOriginalFilename()),options).toString();    }

    public boolean checkFile(FileDto file){
        return file.getFile()!=null && file.getFileType() != null && file.getFilename() != null;
    }
}
