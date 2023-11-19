package com.stud.studadvice.service;

import com.stud.studadvice.exception.AdministrativeProcessException;
import com.stud.studadvice.exception.CategoryException;
import com.stud.studadvice.model.administrative.AdministrativeProcess;
import com.stud.studadvice.repository.administrative.AdministrativeProcessRepository;
import com.stud.studadvice.repository.categories.CategoryRepository;

import org.bson.types.ObjectId;

import com.stud.studadvice.model.administrative.Category;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private AdministrativeProcessRepository administrativeProcessRepository;

    public Page<Category> getCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    public Category getCategoryById(ObjectId categoryId) throws CategoryException {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryException("Category not found"));
    }

    public Category createCategory(Category category) throws AdministrativeProcessException {

        for (AdministrativeProcess administrativeProcess: category.getAdministrativeProcesses()){
                administrativeProcessRepository.findById(administrativeProcess.getId())
                        .orElseThrow(() -> new AdministrativeProcessException("Administrative process not found"));
        }

        return categoryRepository.save(category);
    }

    public Category updateCategoryById(ObjectId categoryId, Category categoryUpdated) throws CategoryException,AdministrativeProcessException {
        Category existingCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryException("Category not found"));

        for (AdministrativeProcess administrativeProcess: categoryUpdated.getAdministrativeProcesses()){
            administrativeProcessRepository.findById(administrativeProcess.getId())
                    .orElseThrow(() -> new AdministrativeProcessException("Administrative process not found"));
        }

        existingCategory.setDescription(categoryUpdated.getDescription());
        existingCategory.setName(categoryUpdated.getName());
        existingCategory.setImage(categoryUpdated.getImage());
        existingCategory.setAdministrativeProcesses(categoryUpdated.getAdministrativeProcesses());

        return categoryRepository.save(existingCategory);

    }

    public void deleteCategoryById(ObjectId categoryId) throws CategoryException {
        categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryException("Category not found"));

        categoryRepository.deleteById(categoryId);
    }

    public Page<AdministrativeProcess> getAdministrativeProcessByCategoryId(ObjectId categoryId, Integer minAge, Integer maxAge, List<String> nationalities, List<String> universities, Pageable pageable) throws CategoryException {
        // TODO
        return null;
    }

    public Category addAdministrativeProcessToAnExistingCategory(ObjectId categoryId, ObjectId administrativeProcessId) throws CategoryException, AdministrativeProcessException {
        Category existingCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryException("Category not found"));

        AdministrativeProcess existingAdministrativeProcess = administrativeProcessRepository.findById(administrativeProcessId)
                .orElseThrow(() -> new AdministrativeProcessException("Administrative process not found"));

        List<AdministrativeProcess> administrativeProcesses = existingCategory.getAdministrativeProcesses();
        administrativeProcesses.add(existingAdministrativeProcess);

        existingCategory.setAdministrativeProcesses(administrativeProcesses);

        return categoryRepository.save(existingCategory);
    }

}
