package com.stud.studadvice.service;

import com.stud.studadvice.dto.DealDto;
import com.stud.studadvice.entity.Deal;
import com.stud.studadvice.exception.DealException;
import com.stud.studadvice.exception.ImageException;
import com.stud.studadvice.repository.deals.DealsRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class DealsServiceTest {

    @Mock
    private GridFsTemplate gridFsTemplate;

    @Mock
    private DealsRepository dealsRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private DealsService dealsService;

    @BeforeEach
    public void setUp() {
    }

    @Test
    void testGetDeals_ShouldReturnPage() {
        // TODO
    }

    @Test
    void testCreateDeal_ValidData_ShouldReturnDto() {
        // TODO
    }

    @Test
    void testCreateDeal_InvalidData_ShouldThrowException() {
        // TODO
    }

    @Test
    void testUpdateDeal_ExistingIdAndValidData_ShouldReturnDto() {
        // TODO
    }

    @Test
    void testUpdateDeal_NonExistingId_ShouldThrowException() {
        // TODO
    }

    @Test
    void testUpdateDeal_InvalidData_ShouldThrowException() {
        // TODO
    }

    @Test
    void testDeleteDeal_ExistingId_ShouldNotThrowException() {
        // TODO
    }

    @Test
    void testDeleteDeal_NonExistingId_ShouldThrowException() {
        // TODO
    }

    @Test
    void testGetDealById_ExistingId_ShouldReturnDto() {
        // TODO
    }

    @Test
    void testGetDealById_NonExistingId_ShouldThrowException() {
        // TODO
    }

    @Test
    void testSearchDeals_WithSearchParams_ShouldReturnPage() {
        // TODO
    }

    @Test
    void testSearchDeals_WithNoSearchParams_ShouldReturnPage() {
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
