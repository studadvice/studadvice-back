package com.stud.studadvice.service;

import com.stud.studadvice.exception.RequiredDocumentException;
import com.stud.studadvice.model.administrative.RequiredDocument;
import com.stud.studadvice.repository.administrative.RequiredDocumentRepository;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequiredDocumentService {

    @Autowired
    private RequiredDocumentRepository requiredDocumentRepository;

    public List<RequiredDocument> getRequiredDocument() {
        return requiredDocumentRepository.findAll();
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
        requiredDocumentRepository.findById(id)
                .orElseThrow(() -> new RequiredDocumentException("Required document not found"));

        requiredDocumentRepository.deleteById(id);
    }
}
