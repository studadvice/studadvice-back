package com.stud.studadvice.service;

import com.stud.studadvice.exception.CategoryException;
import com.stud.studadvice.repository.categories.CategoryRepository;

import org.bson.types.ObjectId;

import com.stud.studadvice.model.administrative.Category;
import com.stud.studadvice.model.administrative.SubCategory;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(ObjectId id) throws CategoryException {
        Optional<Category> existingCategory = categoryRepository.findById(id);
        if (existingCategory.isEmpty()) {
            throw new CategoryException("Category not found");
        }
        else{
            return existingCategory.get();
        }
    }

    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    public Category updateCategoryById(ObjectId id, Category category) throws CategoryException {
        Optional<Category> existingCategory = categoryRepository.findById(id);
        if (existingCategory.isEmpty()) {
            throw new CategoryException("Category not found");
        }
        else{
            Category existingCategoryValue = existingCategory.get();
            existingCategoryValue.setName(category.getName());
            existingCategoryValue.setImage(category.getImage());
            existingCategoryValue.setSubCategoryList(category.getSubCategoryList());
            return categoryRepository.save(existingCategoryValue);
        }
    }

    public void deleteCategoryById(ObjectId id) throws CategoryException {
        if (!categoryRepository.existsById(id)) {
            throw new CategoryException("Category not found");
        }
        categoryRepository.deleteById(id);
    }

    public List<SubCategory> getSubcategoriesOfCategory(String name) {
        Category category = categoryRepository.findByName(name);
        return category != null ? category.getSubCategoryList() : null;
    }

    public SubCategory createSubCategory(ObjectId categoryId, SubCategory subCategory) throws CategoryException {
        Optional<Category> existingCategory = categoryRepository.findById(categoryId);
        if (existingCategory.isEmpty()) {
            throw new CategoryException("Category not found");
        } else {
            Category category = existingCategory.get();
            List<SubCategory> subCategories = category.getSubCategoryList();
            subCategories.add(subCategory);
            category.setSubCategoryList(subCategories);
            categoryRepository.save(category);
            return subCategory;
        }
    }

    public void deleteSubCategoryByName(ObjectId categoryId, String subCategoryName) throws CategoryException {
        Optional<Category> existingCategory = categoryRepository.findById(categoryId);
        if (existingCategory.isEmpty()) {
            throw new CategoryException("Category not found");
        } else {
            Category category = existingCategory.get();
            List<SubCategory> subCategories = category.getSubCategoryList();

            subCategories.removeIf(subCategory -> subCategory.getName().equals(subCategoryName));

            category.setSubCategoryList(subCategories);
            categoryRepository.save(category);
        }
    }

    public SubCategory updateSubCategoryByName(ObjectId categoryId, String subCategoryName, SubCategory updatedSubCategory) throws CategoryException {
        Optional<Category> existingCategory = categoryRepository.findById(categoryId);
        if (existingCategory.isEmpty()) {
            throw new CategoryException("Category not found");
        } else {
            Category category = existingCategory.get();
            List<SubCategory> subCategories = category.getSubCategoryList();
            boolean subCategoryFound = false;

            for (SubCategory subCategory : subCategories) {
                if (subCategory.getName().equals(subCategoryName)) {
                    subCategory.setName(updatedSubCategory.getName());
                    subCategory.setImage(updatedSubCategory.getImage());
                    subCategoryFound = true;
                }
            }

            if (!subCategoryFound) {
                throw new CategoryException("Subcategory not found");
            }

            category.setSubCategoryList(subCategories);
            categoryRepository.save(category);
            return updatedSubCategory;
        }
    }

}
