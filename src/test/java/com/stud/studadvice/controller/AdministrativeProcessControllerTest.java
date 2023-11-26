package com.stud.studadvice.controller;

import com.stud.studadvice.exception.AdministrativeProcessException;
import com.stud.studadvice.model.administrative.AdministrativeProcess;
import com.stud.studadvice.service.AdministrativeProcessService;

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

public class AdministrativeProcessControllerTest {
    @Mock
    private AdministrativeProcessService administrativeProcessService;

    @InjectMocks
    private AdministrativeProcessController administrativeProcessController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAdministrativeProcessById_WhenProcessExists_ShouldReturnProcess() throws AdministrativeProcessException {
        // Given
        ObjectId processId = new ObjectId("6035c09333d5390c6a618f4a");
        AdministrativeProcess expectedProcess = new AdministrativeProcess();
        when(administrativeProcessService.getAdministrativeProcessById(processId)).thenReturn(expectedProcess);

        // When
        AdministrativeProcess actualProcess = administrativeProcessController.getAdministrativeProcessById(processId);

        // Then
        assertNotNull(actualProcess);
        assertEquals(expectedProcess, actualProcess);
    }

    @Test
    void getAdministrativeProcessById_WhenProcessDoesNotExist_ShouldThrowNotFoundException() throws AdministrativeProcessException {
        // Given
        ObjectId processId = new ObjectId("6035c09333d5390c6a618f4a");
        when(administrativeProcessService.getAdministrativeProcessById(processId))
                .thenThrow(new AdministrativeProcessException("Administrative process not found"));

        // When, Then
        assertThrows(ResponseStatusException.class,
                () -> administrativeProcessController.getAdministrativeProcessById(processId));
    }

    @Test
    void createAdministrativeProcess_WhenValidProcess_ShouldReturnCreatedProcess() throws AdministrativeProcessException {
        // Given
        AdministrativeProcess inputProcess = new AdministrativeProcess();
        AdministrativeProcess expectedProcess = new AdministrativeProcess();
        when(administrativeProcessService.createAdministrativeProcess(inputProcess)).thenReturn(expectedProcess);

        // When
        AdministrativeProcess actualProcess = administrativeProcessController.createAdministrativeProcess(inputProcess);

        // Then
        assertNotNull(actualProcess);
        assertEquals(expectedProcess, actualProcess);
    }

    @Test
    void createAdministrativeProcess_WhenInvalidProcess_ShouldThrowBadRequestException() throws AdministrativeProcessException {
        // Given
        AdministrativeProcess inputProcess = new AdministrativeProcess();
        when(administrativeProcessService.createAdministrativeProcess(inputProcess))
                .thenThrow(new AdministrativeProcessException("Bad Request - Invalid input"));

        // When, Then
        assertThrows(ResponseStatusException.class,
                () -> administrativeProcessController.createAdministrativeProcess(inputProcess),
                "Expected BadRequestException");
    }

    @Test
    void updateAdministrativeProcess_WhenValidProcess_ShouldReturnUpdatedProcess() throws AdministrativeProcessException {
        // Given
        ObjectId processId = new ObjectId("6035c09333d5390c6a618f4a");
        AdministrativeProcess updatedProcess = new AdministrativeProcess();
        when(administrativeProcessService.updateAdministrativeProcess(processId, updatedProcess)).thenReturn(updatedProcess);

        // When
        AdministrativeProcess actualProcess = administrativeProcessController.updateAdministrativeProcess(processId, updatedProcess);

        // Then
        assertNotNull(actualProcess);
        assertEquals(updatedProcess, actualProcess);
    }

    @Test
    void updateAdministrativeProcess_WhenProcessDoesNotExist_ShouldReturnNotFoundException() throws AdministrativeProcessException {
        // Given
        ObjectId processId = new ObjectId("6035c09333d5390c6a618f4a");
        AdministrativeProcess updatedProcess = new AdministrativeProcess();
        when(administrativeProcessService.updateAdministrativeProcess(processId, updatedProcess))
                .thenThrow(new AdministrativeProcessException("Administrative process not found"));

        // When, Then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> administrativeProcessController.updateAdministrativeProcess(processId, updatedProcess));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Administrative process not found", exception.getReason());
    }

    @Test
    void deleteAdministrativeProcess_WhenProcessExists_ShouldDeleteProcess() throws AdministrativeProcessException {
        // Given
        ObjectId processId = new ObjectId("6035c09333d5390c6a618f4a");

        // When
        assertDoesNotThrow(() -> administrativeProcessController.deleteAdministrativeProcess(processId));

        // Then: No exception should be thrown
        verify(administrativeProcessService).deleteAdministrativeProcess(processId);
    }

    @Test
    void deleteAdministrativeProcess_WhenProcessDoesNotExist_ShouldReturnNotFoundException() throws AdministrativeProcessException {
        // Given
        ObjectId processId = new ObjectId("6035c09333d5390c6a618f4a");
        doThrow(new AdministrativeProcessException("Administrative process not found"))
                .when(administrativeProcessService).deleteAdministrativeProcess(processId);

        // When, Then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> administrativeProcessController.deleteAdministrativeProcess(processId));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Administrative process not found", exception.getReason());
    }

    @Test
    void getAdministrativeProcesses_WhenProcessesExist_ShouldReturnProcessesPage() {
        // Given
        Integer age = 25;
        String nationality = "French";
        String university = "Example University";
        String education = "Computer Science";
        int page = 0;
        int size = 10;

        Pageable pageable = PageRequest.of(page, size);
        Page expectedPage = mock(Page.class);
        when(administrativeProcessService.getAdministrativeProcesses(age, nationality, university, education, pageable))
                .thenReturn(expectedPage);

        // When
        Page<AdministrativeProcess> actualPage = administrativeProcessController.getAdministrativeProcesses(age, nationality, university, education, page, size);

        // Then
        assertNotNull(actualPage);
        assertEquals(expectedPage, actualPage);
    }

    @Test
    void getAdministrativeProcesses_WhenNoProcessesExist_ShouldReturnEmptyPage() {
        // Given
        int page = 0;
        int size = 10;

        Pageable pageable = PageRequest.of(page, size);
        Page<AdministrativeProcess> expectedPage = Page.empty();
        when(administrativeProcessService.getAdministrativeProcesses(null, null, null, null, pageable))
                .thenReturn(expectedPage);

        // When
        Page<AdministrativeProcess> actualPage = administrativeProcessController.getAdministrativeProcesses(null, null, null, null, page, size);

        // Then
        assertNotNull(actualPage);
        assertTrue(actualPage.isEmpty());
    }

    @Test
    void searchAdministrativeProcess_WhenResultsExist_ShouldReturnResultsPage() {
        // Given
        String searchText = "example";
        int page = 0;
        int size = 10;

        Pageable pageable = PageRequest.of(page, size);
        Page expectedPage = mock(Page.class);
        when(administrativeProcessService.searchAdministrativeProcess(searchText, pageable))
                .thenReturn(expectedPage);

        // When
        Page<AdministrativeProcess> actualPage = administrativeProcessController.searchAdministrativeProcess(searchText, page, size);

        // Then
        assertNotNull(actualPage);
        assertEquals(expectedPage, actualPage);
    }

    @Test
    void searchAdministrativeProcess_WhenNoResultsExist_ShouldReturnEmptyPage() {
        // Given
        String searchText = "nonexistent";
        int page = 0;
        int size = 10;

        Pageable pageable = PageRequest.of(page, size);
        Page<AdministrativeProcess> expectedPage = Page.empty();
        when(administrativeProcessService.searchAdministrativeProcess(searchText, pageable))
                .thenReturn(expectedPage);

        // When
        Page<AdministrativeProcess> actualPage = administrativeProcessController.searchAdministrativeProcess(searchText, page, size);

        // Then
        assertNotNull(actualPage);
        assertTrue(actualPage.isEmpty());
    }

}
