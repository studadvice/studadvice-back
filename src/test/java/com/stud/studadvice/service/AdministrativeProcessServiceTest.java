package com.stud.studadvice.service;

import com.stud.studadvice.exception.AdministrativeProcessException;
import com.stud.studadvice.model.administrative.AdministrativeProcess;
import com.stud.studadvice.model.administrative.RequiredDocument;
import com.stud.studadvice.repository.administrative.AdministrativeProcessRepository;
import com.stud.studadvice.repository.administrative.RequiredDocumentRepository;

import org.bson.types.ObjectId;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AdministrativeProcessServiceTest {
    @Mock
    private MongoTemplate mongoTemplate;
    @Mock
    private AdministrativeProcessRepository administrativeProcessRepository;

    @Mock
    private RequiredDocumentRepository requiredDocumentRepository;

    @InjectMocks
    private AdministrativeProcessService administrativeProcessService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAdministrativeProcessById_WhenProcessExists_ShouldReturnProcess() throws AdministrativeProcessException {
        // Given
        ObjectId processId = new ObjectId("6035c09333d5390c6a618f4a");
        AdministrativeProcess expectedProcess = new AdministrativeProcess();
        when(administrativeProcessRepository.findById(processId)).thenReturn(Optional.of(expectedProcess));

        // When
        AdministrativeProcess actualProcess = administrativeProcessService.getAdministrativeProcessById(processId);

        // Then
        assertNotNull(actualProcess);
        assertEquals(expectedProcess, actualProcess);
    }

    @Test
    void getAdministrativeProcessById_WhenProcessDoesNotExist_ShouldThrowException() {
        // Given
        ObjectId processId = new ObjectId("6035c09333d5390c6a618f4a");
        when(administrativeProcessRepository.findById(processId)).thenReturn(Optional.empty());

        // When, Then
        assertThrows(AdministrativeProcessException.class,
                () -> administrativeProcessService.getAdministrativeProcessById(processId));
    }

//    @Test
//    void createAdministrativeProcess_WhenValidProcess_ShouldReturnCreatedProcess() throws AdministrativeProcessException {
//        // Given
//        AdministrativeProcess inputProcess = new AdministrativeProcess();
//        AdministrativeProcess expectedProcess = new AdministrativeProcess();
//        when(requiredDocumentRepository.findById(any())).thenReturn(Optional.of(new RequiredDocument()));
//        when(administrativeProcessRepository.save(inputProcess)).thenReturn(expectedProcess);
//
//        // When
//        AdministrativeProcess actualProcess = administrativeProcessService.createAdministrativeProcess(inputProcess);
//
//        // Then
//        assertNotNull(actualProcess);
//        assertEquals(expectedProcess, actualProcess);
//    }

//    @Test
//    void createAdministrativeProcess_WhenInvalidProcess_ShouldThrowException() {
//        // Given
//        AdministrativeProcess inputProcess = new AdministrativeProcess();
//        when(requiredDocumentRepository.findById(any())).thenReturn(Optional.of(new RequiredDocument()));
//        when(administrativeProcessRepository.save(inputProcess)).thenThrow(new RuntimeException("Test exception"));
//
//        // When, Then
//        assertThrows(RuntimeException.class,
//                () -> administrativeProcessService.createAdministrativeProcess(inputProcess));
//    }

//    @Test
//    void updateAdministrativeProcess_WhenValidInput_ShouldReturnUpdatedProcess() throws AdministrativeProcessException {
//        // Given
//        ObjectId processId = new ObjectId("6035c09333d5390c6a618f4a");
//        AdministrativeProcess existingProcess = new AdministrativeProcess();
//        AdministrativeProcess updatedProcess = new AdministrativeProcess();
//        updatedProcess.setImage("New Image");
//        updatedProcess.setDescription("New Description");
//
//        when(administrativeProcessRepository.findById(processId)).thenReturn(Optional.of(existingProcess));
//        when(requiredDocumentRepository.findById(any())).thenReturn(Optional.of(new RequiredDocument()));
//        when(administrativeProcessRepository.save(existingProcess)).thenReturn(updatedProcess);
//
//        // When
//        AdministrativeProcess actualProcess = administrativeProcessService.updateAdministrativeProcess(processId, updatedProcess);
//
//        // Then
//        assertNotNull(actualProcess);
//        assertEquals(updatedProcess, actualProcess);
//        assertEquals("New Image", actualProcess.getImage());
//        assertEquals("New Description", actualProcess.getDescription());
//    }

//    @Test
//    void updateAdministrativeProcess_WhenProcessDoesNotExist_ShouldThrowException() {
//        // Given
//        ObjectId processId = new ObjectId("6035c09333d5390c6a618f4a");
//        AdministrativeProcess updatedProcess = new AdministrativeProcess();
//        when(administrativeProcessRepository.findById(processId)).thenReturn(Optional.empty());
//
//        // When, Then
//        assertThrows(AdministrativeProcessException.class,
//                () -> administrativeProcessService.updateAdministrativeProcess(processId, updatedProcess));
//    }

    @Test
    void deleteAdministrativeProcess_WhenProcessExists_ShouldDeleteProcess() throws AdministrativeProcessException {
        // Given
        ObjectId processId = new ObjectId("6035c09333d5390c6a618f4a");
        when(administrativeProcessRepository.existsById(processId)).thenReturn(true);

        // When
        assertDoesNotThrow(() -> administrativeProcessService.deleteAdministrativeProcess(processId));

        // Then: No exception should be thrown
        verify(administrativeProcessRepository).deleteById(processId);
    }

    @Test
    void deleteAdministrativeProcess_WhenProcessDoesNotExist_ShouldThrowException() {
        // Given
        ObjectId processId = new ObjectId("6035c09333d5390c6a618f4a");
        when(administrativeProcessRepository.existsById(processId)).thenReturn(false);

        // When, Then
        assertThrows(AdministrativeProcessException.class,
                () -> administrativeProcessService.deleteAdministrativeProcess(processId));
    }

    @Test
    void getAdministrativeProcesses_WhenCriteriaExist_ShouldReturnProcessesPage() {
        // Given
        Integer age = 25;
        String nationality = "French";
        String university = "Example University";
        String education = "Computer Science";

        when(mongoTemplate.count(any(Query.class), eq(AdministrativeProcess.class))).thenReturn(1L);
        when(mongoTemplate.find(any(Query.class), eq(AdministrativeProcess.class)))
                .thenReturn(new ArrayList<>());

        // When
        Page<AdministrativeProcess> actualPage = administrativeProcessService.getAdministrativeProcesses(age, nationality, university, education, Pageable.unpaged());

        // Then
        assertNotNull(actualPage);
        assertEquals(0, actualPage.getContent().size());
    }

    @Test
    void searchAdministrativeProcess_WhenCriteriaExist_ShouldReturnProcessesPage() {
        // Given
        String searchText = "example";

        when(mongoTemplate.count(any(Query.class), eq(AdministrativeProcess.class))).thenReturn(1L);
        when(mongoTemplate.find(any(Query.class), eq(AdministrativeProcess.class)))
                .thenReturn(new ArrayList<>());

        // When
        Page<AdministrativeProcess> actualPage = administrativeProcessService.searchAdministrativeProcess(searchText, Pageable.unpaged());

        // Then
        assertNotNull(actualPage);
        assertEquals(0, actualPage.getContent().size());
    }
}
