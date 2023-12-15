package com.stud.studadvice.service;

import com.stud.studadvice.dto.AdministrativeProcessDto;
import com.stud.studadvice.exception.AdministrativeProcessException;
import com.stud.studadvice.entity.AdministrativeProcess;
import com.stud.studadvice.entity.RequiredDocument;
import com.stud.studadvice.entity.Step;
import com.stud.studadvice.repository.administrative.AdministrativeProcessRepository;
import com.stud.studadvice.repository.administrative.RequiredDocumentRepository;

import org.bson.types.ObjectId;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class AdministrativeProcessServiceTest {

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private RequiredDocumentRepository requiredDocumentRepository;

    @Mock
    private AdministrativeProcessRepository administrativeProcessRepository;

    @InjectMocks
    private AdministrativeProcessService administrativeProcessService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAdministrativeProcessById_ExistingId_ShouldReturnDto() throws AdministrativeProcessException {
        ObjectId existingId = new ObjectId("657788ec76e962708fc95755");
        AdministrativeProcess existingProcess = new AdministrativeProcess();
        AdministrativeProcessDto expectedDto = new AdministrativeProcessDto();

        when(administrativeProcessRepository.findById(existingId)).thenReturn(Optional.of(existingProcess));
        when(modelMapper.map(existingProcess, AdministrativeProcessDto.class)).thenReturn(expectedDto);

        AdministrativeProcessDto resultDto = administrativeProcessService.getAdministrativeProcessById(existingId);

        assertEquals(expectedDto, resultDto);
    }

    @Test
    void testGetAdministrativeProcessById_NonExistingId_ShouldThrowException() {
        ObjectId nonExistingId = new ObjectId();

        when(administrativeProcessRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        assertThrows(AdministrativeProcessException.class,
                () -> administrativeProcessService.getAdministrativeProcessById(nonExistingId));
    }

    @Test
    void testValidateRequiredDocuments_AllDocumentsExist_ShouldNotThrowException() {
        AdministrativeProcess administrativeProcess = new AdministrativeProcess();
        Step step = new Step();
        RequiredDocument requiredDocument = new RequiredDocument();
        requiredDocument.setId(new ObjectId());
        step.setRequiredDocuments(Collections.singletonList(requiredDocument));
        administrativeProcess.setSteps(Collections.singletonList(step));

        when(requiredDocumentRepository.findById(any())).thenReturn(Optional.of(new RequiredDocument()));

        assertDoesNotThrow(() -> administrativeProcessService.validateRequiredDocuments(administrativeProcess));
    }

    @Test
    void testValidateRequiredDocuments_UndefinedDocument_ShouldThrowException() {
        AdministrativeProcess administrativeProcess = new AdministrativeProcess();
        Step step = new Step();
        RequiredDocument requiredDocument = new RequiredDocument();
        requiredDocument.setId(new ObjectId());
        step.setRequiredDocuments(Collections.singletonList(requiredDocument));
        administrativeProcess.setSteps(Collections.singletonList(step));

        when(requiredDocumentRepository.findById(any())).thenReturn(Optional.empty());

        AdministrativeProcessException exception = assertThrows(AdministrativeProcessException.class,
                () -> administrativeProcessService.validateRequiredDocuments(administrativeProcess));

        assertEquals("Administrative process uses an undefined required document. Please create it first", exception.getMessage());
    }

    @Test
    void testDeleteAdministrativeProcess_ExistingId_ShouldNotThrowException() {
        ObjectId existingId = new ObjectId("657788ec76e962708fc95755");
        when(administrativeProcessRepository.existsById(existingId)).thenReturn(true);

        assertDoesNotThrow(() -> administrativeProcessService.deleteAdministrativeProcess(existingId));
    }

    @Test
    void testDeleteAdministrativeProcess_NonExistingId_ShouldThrowException() {
        ObjectId nonExistingId = new ObjectId("657788ec76e962708fc95755");
        when(administrativeProcessRepository.existsById(nonExistingId)).thenReturn(false);

        assertThrows(AdministrativeProcessException.class,
                () -> administrativeProcessService.deleteAdministrativeProcess(nonExistingId));
    }
}
