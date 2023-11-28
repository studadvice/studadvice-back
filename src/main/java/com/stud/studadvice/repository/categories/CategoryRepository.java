package com.stud.studadvice.repository.categories;

import com.stud.studadvice.entity.administrative.Category;

import org.bson.types.ObjectId;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface CategoryRepository extends MongoRepository<Category, ObjectId> {
}
