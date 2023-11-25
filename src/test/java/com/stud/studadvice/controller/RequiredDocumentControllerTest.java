package com.stud.studadvice.controller;

import com.stud.studadvice.exception.RequiredDocumentException;
import com.stud.studadvice.model.administrative.RequiredDocument;
import com.stud.studadvice.service.RequiredDocumentService;

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

public class RequiredDocumentControllerTest {

    @Mock
    private RequiredDocumentService requiredDocumentService;

    @InjectMocks
    private RequiredDocumentController requiredDocumentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getRequiredDocuments_ShouldReturnRequiredDocumentsPage() {
        // Given
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        Page expectedPage = mock(Page.class);
        when(requiredDocumentService.getRequiredDocuments(pageable)).thenReturn(expectedPage);

        // When
        Page<RequiredDocument> result = requiredDocumentController.getRequiredDocuments(page, size);

        // Then
        assertNotNull(result);
        assertEquals(expectedPage, result);
    }

    @Test
    void getRequiredDocumentById_WhenDocumentExists_ShouldReturnDocument() throws RequiredDocumentException {
        // Given
        ObjectId requiredDocumentId = new ObjectId("6035c09333d5390c6a618f4a");
        RequiredDocument expectedDocument = new RequiredDocument();
        when(requiredDocumentService.getRequiredDocumentById(requiredDocumentId)).thenReturn(expectedDocument);

        // When
        RequiredDocument result = requiredDocumentController.getRequiredDocumentById(requiredDocumentId);

        // Then
        assertNotNull(result);
        assertEquals(expectedDocument, result);
    }

    @Test
    void getRequiredDocumentById_WhenDocumentDoesNotExist_ShouldThrowNotFoundException() throws RequiredDocumentException {
        // Given
        ObjectId requiredDocumentId = new ObjectId("6035c09333d5390c6a618f4a");
        when(requiredDocumentService.getRequiredDocumentById(requiredDocumentId))
                .thenThrow(new RequiredDocumentException("Required document not found"));

        // When, Then
        assertThrows(ResponseStatusException.class,
                () -> requiredDocumentController.getRequiredDocumentById(requiredDocumentId));
    }

    @Test
    void createRequiredDocument_WhenValidDocument_ShouldReturnCreatedDocument() {
        // Given
        RequiredDocument inputDocument = new RequiredDocument();
        RequiredDocument expectedDocument = new RequiredDocument();
        when(requiredDocumentService.createRequiredDocument(inputDocument)).thenReturn(expectedDocument);

        // When
        RequiredDocument result = requiredDocumentController.createRequiredDocument(inputDocument);

        // Then
        assertNotNull(result);
        assertEquals(expectedDocument, result);
    }

    @Test
    void createRequiredDocument_WhenInvalidDocument_ShouldThrowBadRequestException() {
        // Given
        RequiredDocument inputDocument = new RequiredDocument();
        when(requiredDocumentService.createRequiredDocument(inputDocument))
                .thenThrow(new RuntimeException("Bad Request - Invalid input"));

        // When, Then
        assertThrows(RuntimeException.class,
                () -> requiredDocumentController.createRequiredDocument(inputDocument),
                "Expected BadRequestException");
    }

    @Test
    void updateRequiredDocument_WhenValidDocument_ShouldReturnUpdatedDocument() throws RequiredDocumentException {
        // Given
        ObjectId requiredDocumentId = new ObjectId("6035c09333d5390c6a618f4a");
        RequiredDocument updatedDocument = new RequiredDocument();
        when(requiredDocumentService.updateRequiredDocument(requiredDocumentId, updatedDocument)).thenReturn(updatedDocument);

        // When
        RequiredDocument result = requiredDocumentController.updateRequiredDocument(requiredDocumentId, updatedDocument);

        // Then
        assertNotNull(result);
        assertEquals(updatedDocument, result);
    }

    @Test
    void updateRequiredDocument_WhenDocumentDoesNotExist_ShouldThrowNotFoundException() throws RequiredDocumentException {
        // Given
        ObjectId requiredDocumentId = new ObjectId("6035c09333d5390c6a618f4a");
        RequiredDocument updatedDocument = new RequiredDocument();
        when(requiredDocumentService.updateRequiredDocument(requiredDocumentId, updatedDocument))
                .thenThrow(new RequiredDocumentException("Required document not found"));

        // When, Then
        assertThrows(ResponseStatusException.class,
                () -> requiredDocumentController.updateRequiredDocument(requiredDocumentId, updatedDocument));
    }

    @Test
    void deleteRequiredDocument_WhenDocumentExists_ShouldDeleteDocument() throws RequiredDocumentException {
        // Given
        ObjectId requiredDocumentId = new ObjectId("6035c09333d5390c6a618f4a");

        // When
        assertDoesNotThrow(() -> requiredDocumentController.deleteRequiredDocument(requiredDocumentId));

        // Then: No exception should be thrown
        verify(requiredDocumentService).deleteRequiredDocument(requiredDocumentId);
    }

    @Test
    void deleteRequiredDocument_WhenDocumentDoesNotExist_ShouldThrowNotFoundException() throws RequiredDocumentException {
        // Given
        ObjectId requiredDocumentId = new ObjectId("6035c09333d5390c6a618f4a");
        doThrow(new RequiredDocumentException("Required document not found"))
                .when(requiredDocumentService).deleteRequiredDocument(requiredDocumentId);

        // When, Then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> requiredDocumentController.deleteRequiredDocument(requiredDocumentId));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Required document not found", exception.getReason());
    }

    @Test
    void searchRequiredDocuments_WhenResultsExist_ShouldReturnResultsPage() {
        // Given
        String searchText = "example";
        int page = 0;
        int size = 10;

        Pageable pageable = PageRequest.of(page, size);
        Page expectedPage = mock(Page.class);
        when(requiredDocumentService.searchRequiredDocuments(searchText, pageable))
                .thenReturn(expectedPage);

        // When
        Page<RequiredDocument> actualPage = requiredDocumentController.searchRequiredDocuments(searchText, page, size);

        // Then
        assertNotNull(actualPage);
        assertEquals(expectedPage, actualPage);
    }

    @Test
    void searchRequiredDocuments_WhenNoResultsExist_ShouldReturnEmptyPage() {
        // Given
        String searchText = "nonexistent";
        int page = 0;
        int size = 10;

        Pageable pageable = PageRequest.of(page, size);
        Page<RequiredDocument> expectedPage = Page.empty();
        when(requiredDocumentService.searchRequiredDocuments(searchText, pageable))
                .thenReturn(expectedPage);

        // When
        Page<RequiredDocument> actualPage = requiredDocumentController.searchRequiredDocuments(searchText, page, size);

        // Then
        assertNotNull(actualPage);
        assertTrue(actualPage.isEmpty());
    }
}
