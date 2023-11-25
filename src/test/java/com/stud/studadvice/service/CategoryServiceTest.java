package com.stud.studadvice.service;

import com.stud.studadvice.exception.CategoryException;
import com.stud.studadvice.exception.AdministrativeProcessException;
import com.stud.studadvice.model.administrative.AdministrativeProcess;
import com.stud.studadvice.model.administrative.Category;
import com.stud.studadvice.repository.administrative.AdministrativeProcessRepository;
import com.stud.studadvice.repository.categories.CategoryRepository;

import org.bson.types.ObjectId;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private AdministrativeProcessRepository administrativeProcessRepository;

    @InjectMocks
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getCategories_WhenCategoriesExist_ShouldReturnCategoriesPage() {
        // Given
        int page = 0;
        int size = 10;
        Pageable pageable = Pageable.ofSize(size).withPage(page);

        Page<Category> expectedPage = mock(Page.class);
        when(categoryRepository.findAll(pageable)).thenReturn(expectedPage);

        // When
        Page<Category> actualPage = categoryService.getCategories(pageable);

        // Then
        assertNotNull(actualPage);
        assertEquals(expectedPage, actualPage);
    }

    @Test
    void getCategoryById_WhenCategoryExists_ShouldReturnCategory() throws CategoryException {
        // Given
        ObjectId categoryId = new ObjectId("6035c09333d5390c6a618f4a");
        Category expectedCategory = new Category();
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(expectedCategory));

        // When
        Category actualCategory = categoryService.getCategoryById(categoryId);

        // Then
        assertNotNull(actualCategory);
        assertEquals(expectedCategory, actualCategory);
    }

    @Test
    void getCategoryById_WhenCategoryDoesNotExist_ShouldThrowException() {
        // Given
        ObjectId categoryId = new ObjectId("6035c09333d5390c6a618f4a");
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // When, Then
        assertThrows(CategoryException.class,
                () -> categoryService.getCategoryById(categoryId));
    }

    @Test
    void createCategory_WhenValidCategory_ShouldReturnCreatedCategory() throws AdministrativeProcessException {
        // Given
        Category inputCategory = new Category();
        Category expectedCategory = new Category();
        when(administrativeProcessRepository.findById(any())).thenReturn(Optional.of(new AdministrativeProcess()));
        when(categoryRepository.save(inputCategory)).thenReturn(expectedCategory);

        // When
        Category actualCategory = categoryService.createCategory(inputCategory);

        // Then
        assertNotNull(actualCategory);
        assertEquals(expectedCategory, actualCategory);
    }

    @Test
    void createCategory_WhenInvalidCategory_ShouldThrowException() {
        // Given
        Category inputCategory = new Category();
        when(administrativeProcessRepository.findById(any())).thenReturn(Optional.of(new AdministrativeProcess()));
        when(categoryRepository.save(inputCategory)).thenThrow(new RuntimeException("Test exception"));

        // When, Then
        assertThrows(RuntimeException.class,
                () -> categoryService.createCategory(inputCategory));
    }

    @Test
    void updateCategoryById_WhenValidInput_ShouldReturnUpdatedCategory() throws CategoryException, AdministrativeProcessException {
        // Given
        ObjectId categoryId = new ObjectId("6035c09333d5390c6a618f4a");
        Category existingCategory = new Category();
        Category updatedCategory = new Category();
        updatedCategory.setImage("New Image");
        updatedCategory.setDescription("New Description");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(existingCategory));
        when(administrativeProcessRepository.findById(any())).thenReturn(Optional.of(new AdministrativeProcess()));
        when(categoryRepository.save(existingCategory)).thenReturn(updatedCategory);

        // When
        Category actualCategory = categoryService.updateCategoryById(categoryId, updatedCategory);

        // Then
        assertNotNull(actualCategory);
        assertEquals(updatedCategory, actualCategory);
        assertEquals("New Image", actualCategory.getImage());
        assertEquals("New Description", actualCategory.getDescription());
    }

    @Test
    void updateCategoryById_WhenCategoryDoesNotExist_ShouldThrowException() {
        // Given
        ObjectId categoryId = new ObjectId("6035c09333d5390c6a618f4a");
        Category updatedCategory = new Category();
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // When, Then
        assertThrows(CategoryException.class,
                () -> categoryService.updateCategoryById(categoryId, updatedCategory));
    }

    @Test
    void deleteCategoryById_WhenCategoryExists_ShouldDeleteCategory() throws CategoryException {
        // Given
        ObjectId categoryId = new ObjectId("6035c09333d5390c6a618f4a");
        Category existingCategory = new Category();
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.existsById(categoryId)).thenReturn(true);

        // When
        assertDoesNotThrow(() -> categoryService.deleteCategoryById(categoryId));

        // Then: No exception should be thrown, and the delete method should be called
        verify(categoryRepository).deleteById(categoryId);
    }

    @Test
    void deleteCategoryById_WhenCategoryDoesNotExist_ShouldThrowException() {
        // Given
        ObjectId categoryId = new ObjectId("6035c09333d5390c6a618f4a");
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // When, Then
        assertThrows(CategoryException.class,
                () -> categoryService.deleteCategoryById(categoryId));
    }

    @Test
    void getAdministrativeProcessByCategoryId_WhenCategoryExists_ShouldReturnAdministrativeProcessesPage() throws CategoryException {
        // Given
        ObjectId categoryId = new ObjectId("6035c09333d5390c6a618f4a");
        Category category = new Category();
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        List<AdministrativeProcess> administrativeProcesses = new ArrayList<>();
        administrativeProcesses.add(new AdministrativeProcess());
        category.setAdministrativeProcesses(administrativeProcesses);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        // When
        Page<AdministrativeProcess> actualPage = categoryService.getAdministrativeProcessByCategoryId(categoryId, null, null, null, null, Pageable.unpaged());

        // Then
        assertNotNull(actualPage);
        assertEquals(administrativeProcesses, actualPage.getContent());
    }

    @Test
    void getAdministrativeProcessByCategoryId_WhenCategoryDoesNotExist_ShouldThrowException() {
        // Given
        ObjectId categoryId = new ObjectId("6035c09333d5390c6a618f4a");
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // When, Then
        assertThrows(CategoryException.class,
                () -> categoryService.getAdministrativeProcessByCategoryId(categoryId, null, null, null, null, Pageable.unpaged()));
    }

    @Test
    void addAdministrativeProcessToAnExistingCategory_WhenValidInput_ShouldReturnUpdatedCategory() throws CategoryException, AdministrativeProcessException {
        // Given
        ObjectId categoryId = new ObjectId("6035c09333d5390c6a618f4a");
        ObjectId administrativeProcessId = new ObjectId("6035c09333d5390c6a618f4b");
        Category existingCategory = new Category();
        AdministrativeProcess existingAdministrativeProcess = new AdministrativeProcess();

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(existingCategory));
        when(administrativeProcessRepository.findById(administrativeProcessId)).thenReturn(Optional.of(existingAdministrativeProcess));
        when(categoryRepository.save(existingCategory)).thenReturn(existingCategory);

        // When
        Category actualCategory = categoryService.addAdministrativeProcessToAnExistingCategory(categoryId, administrativeProcessId);

        // Then
        assertNotNull(actualCategory);
        assertEquals(existingCategory, actualCategory);
        assertEquals(1, actualCategory.getAdministrativeProcesses().size());
        assertEquals(existingAdministrativeProcess, actualCategory.getAdministrativeProcesses().get(0));
    }

    @Test
    void addAdministrativeProcessToAnExistingCategory_WhenCategoryDoesNotExist_ShouldThrowCategoryException() {
        // Given
        ObjectId categoryId = new ObjectId("6035c09333d5390c6a618f4a");
        ObjectId administrativeProcessId = new ObjectId("6035c09333d5390c6a618f4b");
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // When, Then
        assertThrows(CategoryException.class,
                () -> categoryService.addAdministrativeProcessToAnExistingCategory(categoryId, administrativeProcessId));
    }

    @Test
    void addAdministrativeProcessToAnExistingCategory_WhenAdministrativeProcessDoesNotExist_ShouldThrowAdministrativeProcessException() {
        // Given
        ObjectId categoryId = new ObjectId("6035c09333d5390c6a618f4a");
        ObjectId administrativeProcessId = new ObjectId("6035c09333d5390c6a618f4b");
        Category existingCategory = new Category();

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(existingCategory));
        when(administrativeProcessRepository.findById(administrativeProcessId)).thenReturn(Optional.empty());

        // When, Then
        assertThrows(AdministrativeProcessException.class,
                () -> categoryService.addAdministrativeProcessToAnExistingCategory(categoryId, administrativeProcessId));
    }
}
