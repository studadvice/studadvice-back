package com.stud.studadvice.repository.administrative;

import com.stud.studadvice.model.administrative.RequiredDocument;

import org.bson.types.ObjectId;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequiredDocumentRepository extends MongoRepository<RequiredDocument, ObjectId> {
}
