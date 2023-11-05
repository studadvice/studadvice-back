package com.stud.studadvice.repository.administrative;

import com.stud.studadvice.model.administrative.AdministrativeProcess;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdministrativeProcessRepository extends MongoRepository<AdministrativeProcess, String> {
    @Query(value = "distinct: 'category.name'", fields = "{'category.name' : 1}")
    List<String> findDistinctCategories();

    @Query(value = "{'category.name' : ?0}", fields = "{'category.subCategoriesList.name' : 1}")
    List<String> findDistinctSubCategoriesByCategory(String category);
    @Query(value = "{'category.name' : ?0, 'category.subCategoriesList.name' : ?1}")
    List<AdministrativeProcess> findByCategoryAndSubCategory(String category, String subCategory);
}

