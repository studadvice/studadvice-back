package com.stud.studadvice.repository.faq;

import com.stud.studadvice.entity.Faq;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FaqRepository extends MongoRepository<Faq, ObjectId> {
}
