package com.stud.studadvice.service;

import com.stud.studadvice.exception.AdministrativeProcessException;
import com.stud.studadvice.exception.CategoryException;
import com.stud.studadvice.model.administrative.AdministrativeProcess;
import com.stud.studadvice.repository.administrative.AdministrativeProcessRepository;
import com.stud.studadvice.repository.categories.CategoryRepository;

import org.bson.types.ObjectId;

import com.stud.studadvice.model.administrative.Category;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private AdministrativeProcessRepository administrativeProcessRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
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

        existingCategory.setInformations(categoryUpdated.getInformations());
        existingCategory.setAdministrativeProcesses(categoryUpdated.getAdministrativeProcesses());

        return categoryRepository.save(existingCategory);

    }

    public void deleteCategoryById(ObjectId categoryId) throws CategoryException {
        categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryException("Category not found"));

        categoryRepository.deleteById(categoryId);
    }

    public List<AdministrativeProcess> getAdministrativeProcessByCategoryId(ObjectId categoryId) throws CategoryException {
        Category existingCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryException("Category not found"));

        return existingCategory.getAdministrativeProcesses();
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
