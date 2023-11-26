package com.stud.studadvice.service;

import com.stud.studadvice.exception.RequiredDocumentException;
import com.stud.studadvice.model.administrative.RequiredDocument;
import com.stud.studadvice.repository.administrative.RequiredDocumentRepository;

import org.bson.types.ObjectId;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

public class RequiredDocumentServiceTest {
    @Mock
    private MongoTemplate mongoTemplate;
    @Mock
    private RequiredDocumentRepository requiredDocumentRepository;

    @InjectMocks
    private RequiredDocumentService requiredDocumentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getRequiredDocuments_WhenDocumentsExist_ShouldReturnDocumentsPage() {
        // Given
        List<RequiredDocument> documentsList = new ArrayList<>();
        documentsList.add(new RequiredDocument());
        documentsList.add(new RequiredDocument());

        Page<RequiredDocument> expectedPage = new PageImpl<>(documentsList);

        when(requiredDocumentRepository.findAll(Pageable.unpaged())).thenReturn(expectedPage);

        // When
        Page<RequiredDocument> actualPage = requiredDocumentService.getRequiredDocuments(Pageable.unpaged());

        // Then
        assertNotNull(actualPage);
        assertEquals(expectedPage, actualPage);
    }

    @Test
    void getRequiredDocuments_WhenNoDocumentsExist_ShouldReturnEmptyPage() {
        // Given

        when(requiredDocumentRepository.findAll(Pageable.unpaged())).thenReturn(Page.empty());

        // When
        Page<RequiredDocument> actualPage = requiredDocumentService.getRequiredDocuments(Pageable.unpaged());

        // Then
        assertNotNull(actualPage);
        assertTrue(actualPage.isEmpty());
    }

    @Test
    void getRequiredDocumentById_WhenDocumentExists_ShouldReturnDocument() throws RequiredDocumentException {
        // Given
        ObjectId documentId = new ObjectId("6035c09333d5390c6a618f4a");
        RequiredDocument expectedDocument = new RequiredDocument();
        when(requiredDocumentRepository.findById(documentId)).thenReturn(Optional.of(expectedDocument));

        // When
        RequiredDocument actualDocument = requiredDocumentService.getRequiredDocumentById(documentId);

        // Then
        assertNotNull(actualDocument);
        assertEquals(expectedDocument, actualDocument);
    }

    @Test
    void getRequiredDocumentById_WhenDocumentDoesNotExist_ShouldThrowException() {
        // Given
        ObjectId documentId = new ObjectId("6035c09333d5390c6a618f4a");
        when(requiredDocumentRepository.findById(documentId)).thenReturn(Optional.empty());

        // When, Then
        assertThrows(RequiredDocumentException.class,
                () -> requiredDocumentService.getRequiredDocumentById(documentId));
    }

    @Test
    void createRequiredDocument_WhenValidDocument_ShouldReturnCreatedDocument() {
        // Given
        RequiredDocument inputDocument = new RequiredDocument();
        RequiredDocument expectedDocument = new RequiredDocument();
        when(requiredDocumentRepository.save(inputDocument)).thenReturn(expectedDocument);

        // When
        RequiredDocument actualDocument = requiredDocumentService.createRequiredDocument(inputDocument);

        // Then
        assertNotNull(actualDocument);
        assertEquals(expectedDocument, actualDocument);
    }

    @Test
    void updateRequiredDocument_WhenDocumentExists_ShouldReturnUpdatedDocument() throws RequiredDocumentException {
        // Given
        ObjectId documentId = new ObjectId("6035c09333d5390c6a618f4a");
        RequiredDocument existingDocument = new RequiredDocument();
        when(requiredDocumentRepository.findById(documentId)).thenReturn(Optional.of(existingDocument));

        RequiredDocument updatedDocument = new RequiredDocument();

        when(requiredDocumentRepository.save(existingDocument)).thenReturn(updatedDocument);

        // When
        RequiredDocument actualDocument = requiredDocumentService.updateRequiredDocument(documentId, updatedDocument);

        // Then
        assertNotNull(actualDocument);
        assertEquals(updatedDocument, actualDocument);
    }

    @Test
    void updateRequiredDocument_WhenDocumentDoesNotExist_ShouldThrowException() {
        // Given
        ObjectId documentId = new ObjectId("6035c09333d5390c6a618f4a");
        when(requiredDocumentRepository.findById(documentId)).thenReturn(Optional.empty());

        RequiredDocument updatedDocument = new RequiredDocument();

        // When, Then
        assertThrows(RequiredDocumentException.class,
                () -> requiredDocumentService.updateRequiredDocument(documentId, updatedDocument));
    }

    @Test
    void deleteRequiredDocument_WhenDocumentExists_ShouldDeleteDocument() {
        // Given
        ObjectId documentId = new ObjectId("6035c09333d5390c6a618f4a");
        RequiredDocument existingDocument = new RequiredDocument();
        when(requiredDocumentRepository.findById(documentId)).thenReturn(Optional.of(existingDocument));

        // When
        assertDoesNotThrow(() -> requiredDocumentService.deleteRequiredDocument(documentId));

        // Then: No exception should be thrown
        verify(requiredDocumentRepository).delete(existingDocument);
    }

    @Test
    void deleteRequiredDocument_WhenDocumentDoesNotExist_ShouldThrowException() {
        // Given
        ObjectId documentId = new ObjectId("6035c09333d5390c6a618f4a");
        when(requiredDocumentRepository.findById(documentId)).thenReturn(Optional.empty());

        // When, Then
        assertThrows(RequiredDocumentException.class,
                () -> requiredDocumentService.deleteRequiredDocument(documentId));
    }

    @Test
    void searchRequiredDocuments_WhenResultsExist_ShouldReturnResultsPage() {
        // Given
        String searchText = "example";

        List<RequiredDocument> documentsList = new ArrayList<>();
        documentsList.add(new RequiredDocument());
        documentsList.add(new RequiredDocument());

        Page<RequiredDocument> expectedPage = new PageImpl<>(documentsList);

        TextCriteria criteria = TextCriteria.forDefaultLanguage().matching(searchText);
        Query query = TextQuery.queryText(criteria).sortByScore().with(Pageable.unpaged());

        when(mongoTemplate.count(query, RequiredDocument.class)).thenReturn(expectedPage.getTotalElements());
        when(mongoTemplate.find(query, RequiredDocument.class)).thenReturn(documentsList);

        // When
        Page<RequiredDocument> actualPage = requiredDocumentService.searchRequiredDocuments(searchText, Pageable.unpaged());

        // Then
        assertNotNull(actualPage);
        assertEquals(expectedPage, actualPage);
    }

    @Test
    void searchRequiredDocuments_WhenNoResultsExist_ShouldReturnEmptyPage() {
        // Given
        String searchText = "nonexistent";

        TextCriteria criteria = TextCriteria.forDefaultLanguage().matching(searchText);
        Query query = TextQuery.queryText(criteria).sortByScore().with(Pageable.unpaged());

        when(mongoTemplate.count(query, RequiredDocument.class)).thenReturn(0L);

        // When
        Page<RequiredDocument> actualPage = requiredDocumentService.searchRequiredDocuments(searchText, Pageable.unpaged());

        // Then
        assertNotNull(actualPage);
        assertTrue(actualPage.isEmpty());
    }
}
