package com.stud.studadvice.repository.administrative;

import com.stud.studadvice.model.administrative.AdministrativeProcess;

import org.bson.types.ObjectId;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdministrativeProcessRepository extends MongoRepository<AdministrativeProcess, ObjectId> {
}

