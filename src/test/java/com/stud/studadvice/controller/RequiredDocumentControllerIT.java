package com.stud.studadvice.controller;

import com.stud.studadvice.service.AdministrativeProcessService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class RequiredDocumentControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private AdministrativeProcessService administrativeProcessService;

    @BeforeEach
    public void setUp() {
    }

    @Test
    void givenExistingRequiredDocumentId_whenGetRequiredDocumentById_thenReturnStatusOK() {
        // TODO
    }

    @Test
    void givenInexistingRequiredDocumentId_whenGetRequiredDocumentById_thenReturnStatusNotFound() {
        // TODO
    }

    @Test
    void givenValidInput_whenCreateRequiredDocument_thenReturnStatusCreated() {
        // TODO
    }

    @Test
    void givenInvalidInput_whenCreateRequiredDocument_thenReturnStatusBadRequest() {
        // TODO
    }

    @Test
    void givenRequiredDocumentIdAndValidInput_whenUpdateRequiredDocument_thenReturnStatusOK() {
        // TODO
    }

    @Test
    void givenRequiredDocumentIdAndInvalidInput_whenUpdateRequiredDocument_thenReturnStatusNotFound() {
        // TODO
    }

    @Test
    void givenExistentRequiredDocumentId_whenDeleteRequiredDocument_thenReturnStatusNoContent() {
        // TODO
    }

    @Test
    void givenInexistentRequiredDocumentId_whenDeleteRequiredDocument_thenReturnStatusNoContent() {
        // TODO
    }

    @Test
    void givenSearchText_whenSearchRequiredDocuments_thenStatusOK() {
        // TODO
    }

    @Test
    void givenEmptySearchText_whenSearchRequiredDocuments_thenStatusOK() {
        // TODO
    }

    @Test
    void givenSearchTextWithSpecialCharacters_whenSearchRequiredDocuments_thenStatusOK() {
        // TODO
    }

    @Test
    void givenSearchParams_whenSearchRequiredDocuments_thenStatusOK() {
        // TODO
    }

    @Test
    void givenSearchParamsWithNoFilter_whenSearchRequiredDocuments_thenStatusOK() {
        // TODO
    }

}
