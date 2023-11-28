package com.stud.studadvice.service;

import com.stud.studadvice.dto.AdministrativeProcessDto;
import com.stud.studadvice.dto.CategoryDto;
import com.stud.studadvice.entity.AdministrativeProcess;
import com.stud.studadvice.entity.Category;
import com.stud.studadvice.exception.AdministrativeProcessException;
import com.stud.studadvice.exception.CategoryException;
import com.stud.studadvice.exception.ImageException;
import com.stud.studadvice.repository.administrative.AdministrativeProcessRepository;
import com.stud.studadvice.repository.categories.CategoryRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class CategoryServiceTest {

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private GridFsTemplate gridFsTemplate;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private AdministrativeProcessRepository administrativeProcessRepository;

    @InjectMocks
    private CategoryService categoryService;

    @BeforeEach
    public void setUp() {
    }

    @Test
    void testGetCategories_ShouldReturnPage() {
        // TODO
    }

    @Test
    void testGetCategoryById_ExistingId_ShouldReturnDto() {
        // TODO
    }

    @Test
    void testGetCategoryById_NonExistingId_ShouldThrowException() {
        // TODO
    }

    @Test
    void testCreateCategory_ValidData_ShouldReturnDto() {
        // TODO
    }

    @Test
    void testCreateCategory_InvalidData_ShouldThrowException() {
        // TODO
    }

    @Test
    void testUpdateCategoryById_ExistingIdAndValidData_ShouldReturnDto() {
        // TODO
    }

    @Test
    void testUpdateCategoryById_NonExistingId_ShouldThrowException() {
        // TODO
    }

    @Test
    void testUpdateCategoryById_InvalidData_ShouldThrowException() {
        // TODO
    }

    @Test
    void testDeleteCategoryById_ExistingId_ShouldNotThrowException() {
    // TODO
    }

    @Test
    void testDeleteCategoryById_NonExistingId_ShouldThrowException() {
        // TODO
    }

    @Test
    void testGetAdministrativeProcessByCategoryId_WithSearchParams_ShouldReturnPage() {
        // TODO
    }

    @Test
    void testGetAdministrativeProcessByCategoryId_WithNoSearchParams_ShouldReturnPage() {
        // TODO
    }

    @Test
    void testAddAdministrativeProcessToAnExistingCategory_ExistingIds_ShouldReturnDto() {
        // TODO
    }

    @Test
    void testAddAdministrativeProcessToAnExistingCategory_NonExistingCategory_ShouldThrowException() {
        // TODO
    }

    @Test
    void testAddAdministrativeProcessToAnExistingCategory_NonExistingProcess_ShouldThrowException() {
        // TODO
    }

    @Test
    void testStoreImage_ValidImageFile_ShouldReturnImageId() {
        // TODO
    }

    @Test
    void testStoreImage_InvalidImageFile_ShouldThrowException() {
        // TODO
    }
}
