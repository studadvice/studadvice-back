package com.stud.studadvice.service;

import com.mongodb.client.gridfs.model.GridFSFile;

import com.stud.studadvice.model.File;

import org.apache.commons.io.IOUtils;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class FileService {
    @Autowired
    private GridFsOperations operations;

    @Autowired
    private GridFsTemplate template;
    public File download(String id) throws IOException {

        GridFSFile gridFSFile = template.findOne(new Query(Criteria.where("_id").is(id)));

        File loadFile = new File();

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

    public boolean checkFile(File file){
        return file.getFile()!=null && file.getFileType() != null && file.getFilename() != null;
    }
}
