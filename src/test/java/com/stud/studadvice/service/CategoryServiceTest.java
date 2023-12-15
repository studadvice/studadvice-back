package com.stud.studadvice.service;

import com.stud.studadvice.dto.CategoryDto;
import com.stud.studadvice.entity.AdministrativeProcess;
import com.stud.studadvice.entity.Category;
import com.stud.studadvice.exception.AdministrativeProcessException;
import com.stud.studadvice.exception.CategoryException;
import com.stud.studadvice.repository.administrative.AdministrativeProcessRepository;
import com.stud.studadvice.repository.categories.CategoryRepository;

import org.bson.types.ObjectId;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.modelmapper.ModelMapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class CategoryServiceTest {

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private AdministrativeProcessRepository administrativeProcessRepository;

    @InjectMocks
    private CategoryService categoryService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testGetCategories_ShouldReturnPageOfCategoryDtos() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Category> categories = Collections.singletonList(new Category());
        Page<Category> categoryPage = new PageImpl<>(categories, pageable, 1);
        List<CategoryDto> categoryDtos = Collections.singletonList(new CategoryDto());

        when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);
        when(modelMapper.map(any(Category.class), eq(CategoryDto.class))).thenReturn(new CategoryDto());

        Page<CategoryDto> result = categoryService.getCategories(pageable);

        assertEquals(categoryDtos.size(), result.getContent().size());
        assertEquals(categoryPage.getTotalElements(), result.getTotalElements());

        verify(categoryRepository, times(1)).findAll(pageable);
        verify(modelMapper, times(categories.size())).map(any(Category.class), eq(CategoryDto.class));
    }

    @Test
    void testGetCategoryById_ExistingId_ShouldReturnCategoryDto() throws CategoryException {
        ObjectId categoryId = new ObjectId();
        Category category = new Category();
        category.setId(categoryId);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(modelMapper.map(any(Category.class), eq(CategoryDto.class))).thenReturn(new CategoryDto());

        CategoryDto result = categoryService.getCategoryById(categoryId);

        assertNotNull(result);

        verify(categoryRepository, times(1)).findById(categoryId);
        verify(modelMapper, times(1)).map(any(Category.class), eq(CategoryDto.class));
    }

    @Test
    void testGetCategoryById_NonExistingId_ShouldThrowException() {
        ObjectId categoryId = new ObjectId();

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        CategoryException exception = assertThrows(CategoryException.class,
                () -> categoryService.getCategoryById(categoryId));

        assertEquals("Category not found", exception.getMessage());

        verify(categoryRepository, times(1)).findById(categoryId);
        verify(modelMapper, never()).map(any(), any());
    }

    @Test
    void testValidateAdministrativeProcesses_NoProcesses_ShouldNotThrowException() {
        Category category = new Category();
        category.setAdministrativeProcesses(null);

        assertDoesNotThrow(() -> categoryService.validateAdministrativeProcesses(category));

        verify(administrativeProcessRepository, never()).findById(any());
    }

    @Test
    void testValidateAdministrativeProcesses_AllProcessesExist_ShouldNotThrowException() {
        Category category = new Category();
        AdministrativeProcess administrativeProcess = new AdministrativeProcess();
        administrativeProcess.setId(new ObjectId());
        category.setAdministrativeProcesses(Collections.singletonList(administrativeProcess));

        when(administrativeProcessRepository.findById(any())).thenReturn(Optional.of(administrativeProcess));

        assertDoesNotThrow(() -> categoryService.validateAdministrativeProcesses(category));

        verify(administrativeProcessRepository, times(1)).findById(any());
    }

    @Test
    void testValidateAdministrativeProcesses_UndefinedProcess_ShouldThrowException() {
        Category category = new Category();
        AdministrativeProcess administrativeProcess = new AdministrativeProcess();
        administrativeProcess.setId(new ObjectId());
        category.setAdministrativeProcesses(Collections.singletonList(administrativeProcess));

        when(administrativeProcessRepository.findById(any())).thenReturn(Optional.empty());

        AdministrativeProcessException exception = assertThrows(AdministrativeProcessException.class,
                () -> categoryService.validateAdministrativeProcesses(category));

        assertEquals("Administrative process not found", exception.getMessage());

        verify(administrativeProcessRepository, times(1)).findById(any());
    }

    @Test
    void testDeleteCategoryById_ExistingId_ShouldNotThrowException() {
        ObjectId categoryId = new ObjectId();
        Category category = new Category();
        category.setId(categoryId);

        when(categoryRepository.findById(any())).thenReturn(Optional.of(category));

        assertDoesNotThrow(() -> categoryService.deleteCategoryById(categoryId));

        verify(categoryRepository, times(1)).findById(any());
        verify(categoryRepository, times(1)).delete(category);
    }

    @Test
    void testDeleteCategoryById_NonExistingId_ShouldThrowException() {
        ObjectId categoryId = new ObjectId();

        when(categoryRepository.findById(any())).thenReturn(Optional.empty());

        CategoryException exception = assertThrows(CategoryException.class,
                () -> categoryService.deleteCategoryById(categoryId));

        assertEquals("Category not found", exception.getMessage());

        verify(categoryRepository, times(1)).findById(any());
        verify(categoryRepository, never()).delete(any());
    }

}
