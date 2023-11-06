package com.stud.studadvice.repository.administrative;

import com.stud.studadvice.model.administrative.AdministrativeProcess;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdministrativeProcessRepository extends MongoRepository<AdministrativeProcess, ObjectId> {

    List<AdministrativeProcess> findByCategoryNameAndSubCategoryName(String categoryName, String subCategoryName);
    List<AdministrativeProcess> findByCategoryName(String categoryName);

    List<AdministrativeProcess> findBySubCategoryName(String subCategoryName);
}

