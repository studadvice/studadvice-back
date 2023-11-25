package com.stud.studadvice.controller;

import com.stud.studadvice.exception.AdministrativeProcessException;
import com.stud.studadvice.exception.CategoryException;
import com.stud.studadvice.model.administrative.AdministrativeProcess;
import com.stud.studadvice.model.administrative.Category;
import com.stud.studadvice.service.CategoryService;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.validation.Valid;

import org.bson.types.ObjectId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Operation(summary = "Get all categories")
    @GetMapping
    public Page<Category> getCategories(@RequestParam(defaultValue = "${spring.data.web.pageable.default-page}") int page,
                                        @RequestParam(defaultValue = "${spring.data.web.pageable.default-page-size}") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return categoryService.getCategories(pageable);
    }

    @Operation(summary = "Get a category by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category found", content = @Content(schema = @Schema(implementation = Category.class))),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @GetMapping("/{categoryId}")
    public Category getCategoryById(@PathVariable ObjectId categoryId) {
        try {
            return categoryService.getCategoryById(categoryId);
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
    public Category createCategory(@Valid @RequestBody Category category) {
        try {
            return categoryService.createCategory(category);
        }
        catch (AdministrativeProcessException administrativeProcessException){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, administrativeProcessException.getMessage(), administrativeProcessException);
        }
    }

    @Operation(summary = "Update a category by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category updated successfully", content = @Content(schema = @Schema(implementation = Category.class))),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @PutMapping("/{categoryId}")
    public Category updateCategoryById(@PathVariable ObjectId categoryId, @Valid @RequestBody Category category) {
        try{
            return categoryService.updateCategoryById(categoryId, category);
        }
        catch (CategoryException categoryException) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, categoryException.getMessage(), categoryException);
        }
        catch (AdministrativeProcessException administrativeProcessException) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, administrativeProcessException.getMessage(), administrativeProcessException);
        }
    }

    @Operation(summary = "Delete a category by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Category deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @DeleteMapping("/{categoryId}")
    public void deleteCategoryById(@PathVariable ObjectId categoryId) {
        try {
            categoryService.deleteCategoryById(categoryId);
        }
        catch (CategoryException categoryException) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, categoryException.getMessage(), categoryException);
        }
    }
    @Operation(summary = "Get all the administrative processes of a category by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @GetMapping("/{categoryId}/administrative-process")
    public Page<AdministrativeProcess> getAdministrativeProcessByCategoryId(@PathVariable ObjectId categoryId,
                                                                            @RequestParam(required = false) Integer age,
                                                                            @RequestParam(required = false) String nationality,
                                                                            @RequestParam(required = false) String university,
                                                                            @RequestParam(required = false) String education,
                                                                            @RequestParam(defaultValue = "${spring.data.web.pageable.default-page}") int page,
                                                                            @RequestParam(defaultValue = "${spring.data.web.pageable.default-page-size}") int size) {
        Pageable pageable = PageRequest.of(page, size);
        try {
            return categoryService.getAdministrativeProcessByCategoryId(categoryId, age,nationality, university, education,pageable);
        }
        catch (CategoryException categoryException) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, categoryException.getMessage(), categoryException);
        }
    }

    @PostMapping("/{categoryId})")
    public Category addAdministrativeProcessToAnExistingCategory(@PathVariable ObjectId categoryId,@RequestParam ObjectId administrativeProcessId){
        try {
            return categoryService.addAdministrativeProcessToAnExistingCategory(categoryId,administrativeProcessId);
        }
        catch (CategoryException categoryException) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, categoryException.getMessage(), categoryException);
        }
        catch (AdministrativeProcessException administrativeProcessException) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, administrativeProcessException.getMessage(), administrativeProcessException);
        }
    }

}
