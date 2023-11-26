package com.stud.studadvice.service;

import com.stud.studadvice.exception.RequiredDocumentException;
import com.stud.studadvice.model.administrative.RequiredDocument;
import com.stud.studadvice.repository.administrative.RequiredDocumentRepository;

import org.bson.types.ObjectId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequiredDocumentService {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private RequiredDocumentRepository requiredDocumentRepository;

    public Page<RequiredDocument> getRequiredDocuments(Pageable pageable) {
        return requiredDocumentRepository.findAll(pageable);
    }

    public RequiredDocument getRequiredDocumentById(ObjectId id) throws RequiredDocumentException {
        return requiredDocumentRepository.findById(id)
                .orElseThrow(() -> new RequiredDocumentException("Required document not found"));
    }

    public RequiredDocument createRequiredDocument(RequiredDocument requiredDocument) {
        return requiredDocumentRepository.save(requiredDocument);
    }

    public RequiredDocument updateRequiredDocument(ObjectId id, RequiredDocument requiredDocumentUpdated) throws RequiredDocumentException {
        RequiredDocument requiredDocument = requiredDocumentRepository.findById(id)
                .orElseThrow(() -> new RequiredDocumentException("Required document not found"));

        requiredDocument.setDescription(requiredDocumentUpdated.getDescription());
        requiredDocument.setImage(requiredDocument.getImage());
        requiredDocument.setName(requiredDocument.getName());

        return requiredDocumentRepository.save(requiredDocument);
    }

    public void deleteRequiredDocument(ObjectId id) throws RequiredDocumentException {
        RequiredDocument requiredDocument = requiredDocumentRepository.findById(id)
                .orElseThrow(() -> new RequiredDocumentException("Required document not found"));

        requiredDocumentRepository.delete(requiredDocument);
    }

    public Page<RequiredDocument> searchRequiredDocuments(String searchText, Pageable pageable) {
        TextCriteria criteria = TextCriteria.forDefaultLanguage()
                .matching(searchText);

        Query query = TextQuery.queryText(criteria)
                .sortByScore();

        query.with(pageable);

        long total = mongoTemplate.count(query, RequiredDocument.class);

        List<RequiredDocument> processes = mongoTemplate.find(query, RequiredDocument.class);

        return PageableExecutionUtils.getPage(processes, pageable, () -> total);
    }
}
