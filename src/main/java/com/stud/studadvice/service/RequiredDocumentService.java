package com.stud.studadvice.service;

import com.mongodb.client.gridfs.model.GridFSUploadOptions;

import com.stud.studadvice.dto.RequiredDocumentDto;
import com.stud.studadvice.exception.ImageException;
import com.stud.studadvice.exception.RequiredDocumentException;
import com.stud.studadvice.entity.RequiredDocument;
import com.stud.studadvice.repository.administrative.RequiredDocumentRepository;

import org.bson.Document;
import org.bson.types.ObjectId;

import org.modelmapper.ModelMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
public class RequiredDocumentService {

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private RequiredDocumentRepository requiredDocumentRepository;

    @Autowired
    private ModelMapper modelMapper;

    public Page<RequiredDocumentDto> getRequiredDocuments(Pageable pageable) {
        Page<RequiredDocument> requiredDocuments = requiredDocumentRepository.findAll(pageable);
        return requiredDocuments.map(document -> modelMapper.map(document, RequiredDocumentDto.class));
    }

    public RequiredDocumentDto getRequiredDocumentById(ObjectId id) throws RequiredDocumentException {
        RequiredDocument requiredDocument = requiredDocumentRepository.findById(id)
                .orElseThrow(() -> new RequiredDocumentException("Required document not found"));
        return modelMapper.map(requiredDocument, RequiredDocumentDto.class);
    }

    public RequiredDocumentDto createRequiredDocument(RequiredDocument requiredDocument, MultipartFile imageFile)
            throws ImageException {
        processImage(requiredDocument, imageFile);

        RequiredDocument savedDocument = requiredDocumentRepository.save(requiredDocument);
        return modelMapper.map(savedDocument, RequiredDocumentDto.class);
    }

    public RequiredDocumentDto updateRequiredDocument(ObjectId id, RequiredDocument requiredDocument, MultipartFile imageFile)
            throws RequiredDocumentException, ImageException {
        RequiredDocument existingDocument = requiredDocumentRepository.findById(id)
                .orElseThrow(() -> new RequiredDocumentException("Required document not found"));

        existingDocument.setName(requiredDocument.getName());
        existingDocument.setDescription(requiredDocument.getDescription());
        existingDocument.setUrl(requiredDocument.getUrl());
        processImage(existingDocument, imageFile);

        RequiredDocument updatedDocument = requiredDocumentRepository.save(existingDocument);
        return modelMapper.map(updatedDocument, RequiredDocumentDto.class);
    }

    private void processImage(RequiredDocument requiredDocument, MultipartFile imageFile) throws ImageException {
        if (imageFile != null) {
            try {
                String imageId = storeImage(imageFile);
                requiredDocument.setImageId(imageId);
            } catch (IOException ioException) {
                throw new ImageException("Error when storing the image");
            }
        }
    }


    public void deleteRequiredDocument(ObjectId id) throws RequiredDocumentException {
        RequiredDocument requiredDocument = requiredDocumentRepository.findById(id)
                .orElseThrow(() -> new RequiredDocumentException("Required document not found"));

        requiredDocumentRepository.delete(requiredDocument);
    }

    public Page<RequiredDocumentDto> searchRequiredDocuments(String searchText, Pageable pageable) {
        TextCriteria criteria = TextCriteria.forDefaultLanguage().matching(searchText);
        Query query = TextQuery.queryText(criteria).sortByScore();
        query.with(pageable);

        long total = mongoTemplate.count(query, RequiredDocument.class);
        List<RequiredDocument> documents = mongoTemplate.find(query, RequiredDocument.class);
        List<RequiredDocumentDto> documentDtos = documents.stream().map(document -> modelMapper.map(document, RequiredDocumentDto.class)).toList();

        return PageableExecutionUtils.getPage(documentDtos, pageable, () -> total);
    }

    public String storeImage(MultipartFile imageFile) throws IOException {
        GridFSUploadOptions options = new GridFSUploadOptions()
                .metadata(new Document("contentType", imageFile.getContentType())
                        .append("contentSize", imageFile.getSize()));
        return gridFsTemplate.store(imageFile.getInputStream(), Objects.requireNonNull(imageFile.getOriginalFilename()), options).toString();
    }
}
