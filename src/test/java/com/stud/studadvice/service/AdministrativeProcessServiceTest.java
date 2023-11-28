package com.stud.studadvice.service;

import com.stud.studadvice.dto.AdministrativeProcessDto;
import com.stud.studadvice.exception.AdministrativeProcessException;
import com.stud.studadvice.exception.ImageException;
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
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class AdministrativeProcessServiceTest {

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private RequiredDocumentRepository requiredDocumentRepository;

    @Mock
    private AdministrativeProcessRepository administrativeProcessRepository;

    @Mock
    private GridFsTemplate gridFsTemplate;

    @InjectMocks
    private AdministrativeProcessService administrativeProcessService;

    @BeforeEach
    public void setUp() {
    }

    @Test
    void testGetAdministrativeProcessById_ExistingId_ShouldReturnDto() {
        // TODO
    }

    @Test
    void testGetAdministrativeProcessById_NonExistingId_ShouldThrowException() {
        // TODO
    }

    @Test
    void testCreateAdministrativeProcess_ValidData_ShouldReturnDto() {
        // TODO
    }

    @Test
    void testCreateAdministrativeProcess_InvalidData_ShouldThrowException() {
        // TODO
    }

    @Test
    void testUpdateAdministrativeProcess_ExistingIdAndValidData_ShouldReturnDto() {
        // TODO
    }

    @Test
    void testUpdateAdministrativeProcess_NonExistingId_ShouldThrowException() {
        // TODO
    }

    @Test
    void testUpdateAdministrativeProcess_InvalidData_ShouldThrowException() {
        // TODO
    }

    @Test
    void testDeleteAdministrativeProcess_ExistingId_ShouldNotThrowException() {
        // TODO
    }

    @Test
    void testDeleteAdministrativeProcess_NonExistingId_ShouldThrowException() {
        // TODO
    }

    @Test
    void testGetAdministrativeProcesses_WithSearchParams_ShouldReturnPage() {
        // TODO
    }

    @Test
    void testSearchAdministrativeProcess_WithSearchText_ShouldReturnPage() {
        // TODO
    }

    @Test
    void testSearchAdministrativeProcess_WithEmptySearchText_ShouldReturnPage() {
        // TODO
    }

    @Test
    void testSearchAdministrativeProcess_WithSpecialCharacters_ShouldReturnPage() {
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
