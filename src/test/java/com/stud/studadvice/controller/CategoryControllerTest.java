package com.stud.studadvice.controller;

import com.stud.studadvice.exception.AdministrativeProcessException;
import com.stud.studadvice.exception.CategoryException;
import com.stud.studadvice.model.administrative.AdministrativeProcess;
import com.stud.studadvice.model.administrative.Category;
import com.stud.studadvice.service.CategoryService;

import org.bson.types.ObjectId;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CategoryControllerTest {
    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getCategories_WhenCategoriesExist_ShouldReturnCategoriesPage() {
        // Given
        int page = 0;
        int size = 10;

        Pageable pageable = PageRequest.of(page, size);
        Page expectedPage = mock(Page.class);
        when(categoryService.getCategories(pageable)).thenReturn(expectedPage);

        // When
        Page<Category> actualPage = categoryController.getCategories(page, size);

        // Then
        assertNotNull(actualPage);
        assertEquals(expectedPage, actualPage);
    }

    @Test
    void getCategoryById_WhenCategoryExists_ShouldReturnCategory() throws CategoryException {
        // Given
        ObjectId categoryId = new ObjectId("6035c09333d5390c6a618f4a");
        Category expectedCategory = new Category();
        when(categoryService.getCategoryById(categoryId)).thenReturn(expectedCategory);

        // When
        Category actualCategory = categoryController.getCategoryById(categoryId);

        // Then
        assertNotNull(actualCategory);
        assertEquals(expectedCategory, actualCategory);
    }

    @Test
    void getCategoryById_WhenCategoryDoesNotExist_ShouldThrowNotFoundException() throws CategoryException {
        // Given
        ObjectId categoryId = new ObjectId("6035c09333d5390c6a618f4a");
        when(categoryService.getCategoryById(categoryId))
                .thenThrow(new CategoryException("Category not found"));

        // When, Then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> categoryController.getCategoryById(categoryId));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Category not found", exception.getReason());
    }

    @Test
    void createCategory_WhenValidCategory_ShouldReturnCreatedCategory() throws AdministrativeProcessException {
        // Given
        Category inputCategory = new Category();
        Category expectedCategory = new Category();
        when(categoryService.createCategory(inputCategory)).thenReturn(expectedCategory);

        // When
        Category actualCategory = categoryController.createCategory(inputCategory);

        // Then
        assertNotNull(actualCategory);
        assertEquals(expectedCategory, actualCategory);
    }

    @Test
    void createCategory_WhenInvalidCategory_ShouldThrowBadRequestException() throws AdministrativeProcessException {
        // Given
        Category inputCategory = new Category();
        when(categoryService.createCategory(inputCategory))
                .thenThrow(new AdministrativeProcessException("Bad Request - Invalid input"));

        // When, Then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> categoryController.createCategory(inputCategory));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Bad Request - Invalid input", exception.getReason());
    }
    @Test
    void updateCategoryById_WhenValidCategory_ShouldReturnUpdatedCategory() throws CategoryException, AdministrativeProcessException {
        // Given
        ObjectId categoryId = new ObjectId("6035c09333d5390c6a618f4a");
        Category updatedCategory = new Category();
        when(categoryService.updateCategoryById(categoryId, updatedCategory)).thenReturn(updatedCategory);

        // When
        Category actualCategory = categoryController.updateCategoryById(categoryId, updatedCategory);

        // Then
        assertNotNull(actualCategory);
        assertEquals(updatedCategory, actualCategory);
    }

    @Test
    void updateCategoryById_WhenCategoryDoesNotExist_ShouldThrowNotFoundException() throws CategoryException, AdministrativeProcessException {
        // Given
        ObjectId categoryId = new ObjectId("6035c09333d5390c6a618f4a");
        Category updatedCategory = new Category();
        when(categoryService.updateCategoryById(categoryId, updatedCategory))
                .thenThrow(new CategoryException("Category not found"));

        // When, Then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> categoryController.updateCategoryById(categoryId, updatedCategory));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Category not found", exception.getReason());
    }

    @Test
    void updateCategoryById_WhenUpdateFails_ShouldThrowNotFoundException() throws CategoryException, AdministrativeProcessException {
        // Given
        ObjectId categoryId = new ObjectId("6035c09333d5390c6a618f4a");
        Category updatedCategory = new Category();
        when(categoryService.updateCategoryById(categoryId, updatedCategory))
                .thenThrow(new AdministrativeProcessException("Update failed"));

        // When, Then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> categoryController.updateCategoryById(categoryId, updatedCategory));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Update failed", exception.getReason());
    }

    @Test
    void deleteCategoryById_WhenCategoryExists_ShouldDeleteCategory() throws CategoryException {
        // Given
        ObjectId categoryId = new ObjectId("6035c09333d5390c6a618f4a");

        // When
        assertDoesNotThrow(() -> categoryController.deleteCategoryById(categoryId));

        // Then: No exception should be thrown
        verify(categoryService).deleteCategoryById(categoryId);
    }

    @Test
    void deleteCategoryById_WhenCategoryDoesNotExist_ShouldThrowNotFoundException() throws CategoryException {
        // Given
        ObjectId categoryId = new ObjectId("6035c09333d5390c6a618f4a");
        doThrow(new CategoryException("Category not found"))
                .when(categoryService).deleteCategoryById(categoryId);

        // When, Then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> categoryController.deleteCategoryById(categoryId));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Category not found", exception.getReason());
    }

    @Test
    void getAdministrativeProcessByCategoryId_WhenCategoryExists_ShouldReturnProcessesPage() throws CategoryException {
        // Given
        ObjectId categoryId = new ObjectId("6035c09333d5390c6a618f4a");
        Integer age = 25;
        String nationality = "French";
        String university = "Example University";
        String education = "Computer Science";
        int page = 0;
        int size = 10;

        Pageable pageable = PageRequest.of(page, size);
        Page expectedPage = mock(Page.class);
        when(categoryService.getAdministrativeProcessByCategoryId(categoryId, age, nationality, university, education, pageable))
                .thenReturn(expectedPage);

        // When
        Page<AdministrativeProcess> actualPage = categoryController.getAdministrativeProcessByCategoryId(categoryId, age, nationality, university, education, page, size);

        // Then
        assertNotNull(actualPage);
        assertEquals(expectedPage, actualPage);
    }

    @Test
    void getAdministrativeProcessByCategoryId_WhenCategoryDoesNotExist_ShouldThrowNotFoundException() throws CategoryException {
        // Given
        ObjectId categoryId = new ObjectId("6035c09333d5390c6a618f4a");
        Integer age = 25;
        String nationality = "French";
        String university = "Example University";
        String education = "Computer Science";
        int page = 0;
        int size = 10;

        Pageable pageable = PageRequest.of(page, size);
        when(categoryService.getAdministrativeProcessByCategoryId(categoryId, age, nationality, university, education, pageable))
                .thenThrow(new CategoryException("Category not found"));

        // When, Then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> categoryController.getAdministrativeProcessByCategoryId(categoryId, age, nationality, university, education, page, size));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Category not found", exception.getReason());
    }

    @Test
    void addAdministrativeProcessToAnExistingCategory_WhenValidParameters_ShouldReturnUpdatedCategory() throws CategoryException, AdministrativeProcessException {
        // Given
        ObjectId categoryId = new ObjectId("6035c09333d5390c6a618f4a");
        ObjectId administrativeProcessId = new ObjectId("6035c09333d5390c6a618f4b");
        Category updatedCategory = new Category();
        when(categoryService.addAdministrativeProcessToAnExistingCategory(categoryId, administrativeProcessId))
                .thenReturn(updatedCategory);

        // When
        Category actualCategory = categoryController.addAdministrativeProcessToAnExistingCategory(categoryId, administrativeProcessId);

        // Then
        assertNotNull(actualCategory);
        assertEquals(updatedCategory, actualCategory);
    }

    @Test
    void addAdministrativeProcessToAnExistingCategory_WhenCategoryDoesNotExist_ShouldThrowNotFoundException() throws CategoryException, AdministrativeProcessException {
        // Given
        ObjectId categoryId = new ObjectId("6035c09333d5390c6a618f4a");
        ObjectId administrativeProcessId = new ObjectId("6035c09333d5390c6a618f4b");
        when(categoryService.addAdministrativeProcessToAnExistingCategory(categoryId, administrativeProcessId))
                .thenThrow(new CategoryException("Category not found"));

        // When, Then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> categoryController.addAdministrativeProcessToAnExistingCategory(categoryId, administrativeProcessId));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Category not found", exception.getReason());
    }

    @Test
    void addAdministrativeProcessToAnExistingCategory_WhenAdditionFails_ShouldThrowNotFoundException() throws CategoryException, AdministrativeProcessException {
        // Given
        ObjectId categoryId = new ObjectId("6035c09333d5390c6a618f4a");
        ObjectId administrativeProcessId = new ObjectId("6035c09333d5390c6a618f4b");
        when(categoryService.addAdministrativeProcessToAnExistingCategory(categoryId, administrativeProcessId))
                .thenThrow(new AdministrativeProcessException("Addition failed"));

        // When, Then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> categoryController.addAdministrativeProcessToAnExistingCategory(categoryId, administrativeProcessId));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Addition failed", exception.getReason());
    }
}
