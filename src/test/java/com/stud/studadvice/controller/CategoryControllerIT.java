package com.stud.studadvice.controller;

import com.stud.studadvice.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class CategoryControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private CategoryService categoryService;

    @BeforeEach
    public void setUp() {
    }

    @Test
    void givenExistingCategoryId_whenGetCategoryById_thenReturnStatusOK() {
        // TODO
    }

    @Test
    void givenInexistingCategoryId_whenGetCategoryById_thenReturnStatusNotFound() {
        // TODO
    }

    @Test
    void givenValidInput_whenCreateCategory_thenReturnStatusCreated() {
        // TODO
    }

    @Test
    void givenValidInputWithNoImage_whenCreateCategory_thenReturnStatusBadRequest() {
        // TODO
    }

    @Test
    void givenInvalidInput_whenCreateCategory_thenReturnStatusBadRequest() {
        // TODO
    }

    @Test
    void givenCategoryIdAndValidInput_whenUpdateCategory_thenReturnStatusOK() {
        // TODO
    }

    @Test
    void givenCategoryIdAndInvalidInput_whenUpdateCategory_thenReturnStatusNotFound() {
        // TODO
    }

    @Test
    void givenExistentCategoryId_whenDeleteAdministrativeProcess_thenReturnStatusNoContent() {
        // TODO
    }

    @Test
    void givenInexistentCategoryId_whenDeleteCategory_thenReturnStatusNoContent() {
        // TODO
    }
    @Test
    void givenExistingCategoryId_whenGetAdministrativeProcessByCategoryId_thenStatusOK() {
        // TODO
    }

    @Test
    void givenInexistingCategoryId_whenGetAdministrativeProcessByCategoryId_thenStatusNotFound() {
        // TODO
    }

    @Test
    void givenSearchParams_whenGetAdministrativeProcessByCategoryId_thenStatusOK() {
        // TODO
    }

    @Test
    void givenSearchParamsWithNoFilter_whenGetAdministrativeProcessByCategoryId_thenStatusOK() {
        // TODO
    }

    @Test
    void givenExistingCategoryIdAndAdministrativeProcessId_whenAddProcessToCategory_thenStatusOK() {
        // TODO
    }

    @Test
    void givenInexistingCategoryId_whenAddProcessToCategory_thenStatusNotFound() {
        // TODO
    }

    @Test
    void givenCategoryIdAndInexistingAdministrativeProcessId_whenAddProcessToCategory_thenStatusNotFound() {
        // TODO
    }

    @Test
    void givenCategoryIdAndExistingAdministrativeProcessId_whenAddProcessToCategory_thenStatusOK() {
        // TODO
    }

    @Test
    void givenCategoryIdAndAlreadyAssociatedAdministrativeProcess_whenAddProcessToCategory_thenStatusConflict() {
        // TODO
    }
}
