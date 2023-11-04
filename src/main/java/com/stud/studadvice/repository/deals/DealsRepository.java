package com.stud.studadvice.repository.deals;

import com.stud.studadvice.model.deals.Deals;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DealsRepository extends MongoRepository<Deals, String> {
}
