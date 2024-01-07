package com.stud.studadvice.repository.deals;

import com.stud.studadvice.entity.Deal;

import org.bson.types.ObjectId;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DealsRepository extends MongoRepository<Deal, ObjectId> {

}
