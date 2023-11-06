package com.stud.studadvice.controller;

import com.stud.studadvice.exception.CategoryException;
import com.stud.studadvice.model.administrative.Category;
import com.stud.studadvice.model.administrative.SubCategory;
import com.stud.studadvice.service.CategoryService;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.bson.types.ObjectId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @Operation(summary = "Get all categories")
    @GetMapping
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @Operation(summary = "Get a category by name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category found", content = @Content(schema = @Schema(implementation = Category.class))),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @GetMapping("/{id}")
    public Category getCategoryById(@PathVariable ObjectId id) {
        try {
            return categoryService.getCategoryById(id);
        }
        catch (CategoryException categoryException) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, categoryException.getMessage(), categoryException);
        }
    }

    @Operation(summary = "Create a new category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Category created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request - Invalid input"),
    })
    @PostMapping
    public Category createCategory(@RequestBody Category category) {
        return categoryService.createCategory(category);
    }

    @Operation(summary = "Update a category by name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category updated successfully", content = @Content(schema = @Schema(implementation = Category.class))),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @PutMapping("/{id}")
    public Category updateCategoryById(@PathVariable ObjectId id, @RequestBody Category category) {
        try{
            return categoryService.updateCategoryById(id, category);
        }
        catch (CategoryException categoryException) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, categoryException.getMessage(), categoryException);
        }
    }

    @Operation(summary = "Delete a category by name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Category deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @DeleteMapping("/{id}")
    public void deleteCategoryById(@PathVariable ObjectId id) {
        try {
            categoryService.deleteCategoryById(id);
        }
        catch (CategoryException categoryException) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, categoryException.getMessage(), categoryException);
        }
    }


    @Operation(summary = "Get all subcategories of a category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subcategories found", content = @Content(array = @ArraySchema(schema = @Schema(implementation = SubCategory.class)))),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @GetMapping("/{name}/subcategories")
    public List<SubCategory> getSubcategoriesOfCategory(@PathVariable String name) {
        return categoryService.getSubcategoriesOfCategory(name);
    }


    @Operation(summary = "Create a subcategory for a category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Subcategory created successfully", content = @Content(schema = @Schema(implementation = SubCategory.class))),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @PostMapping("/{categoryId}/subcategories")
    public SubCategory createSubCategoryForCategory(@PathVariable ObjectId categoryId, @RequestBody SubCategory subCategory) {
        try {
            return categoryService.createSubCategory(categoryId, subCategory);
        } catch (CategoryException categoryException) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, categoryException.getMessage(), categoryException);
        }
    }


    @Operation(summary = "Update a subcategory by name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subcategory updated successfully", content = @Content(schema = @Schema(implementation = SubCategory.class))),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @PutMapping("/{categoryId}/subcategories/{subCategoryName}")
    public SubCategory updateSubCategoryByName(@PathVariable ObjectId categoryId, @PathVariable String subCategoryName, @RequestBody SubCategory updatedSubCategory) {
        try {
            return categoryService.updateSubCategoryByName(categoryId, subCategoryName, updatedSubCategory);
        } catch (CategoryException categoryException) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, categoryException.getMessage(), categoryException);
        }
    }

    @Operation(summary = "Delete a subcategory by name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Subcategory deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @DeleteMapping("/{categoryId}/subcategories/{subCategoryName}")
    public void deleteSubCategoryByName(@PathVariable ObjectId categoryId, @PathVariable String subCategoryName) {
        try {
            categoryService.deleteSubCategoryByName(categoryId, subCategoryName);
        } catch (CategoryException categoryException) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, categoryException.getMessage(), categoryException);
        }
    }
}
