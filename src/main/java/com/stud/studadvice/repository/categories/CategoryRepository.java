package com.stud.studadvice.repository.categories;

import com.stud.studadvice.model.administrative.Category;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CategoryRepository extends MongoRepository<Category, ObjectId> {
    Category findByName(String name);
}
