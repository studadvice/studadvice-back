package com.stud.studadvice.service;

import com.stud.studadvice.exception.DealException;
import com.stud.studadvice.entity.Deal;

import com.stud.studadvice.repository.deals.DealsRepository;
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

public class DealsServiceTest {
    @Mock
    private DealsRepository dealsRepository;

    @Mock
    private Page<Deal> mockPage;

    @InjectMocks
    private DealsService dealsService;

    @Mock
    private MongoTemplate mongoTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getDeals_ShouldReturnDealsPage() {
        // Given
        Pageable pageable = mock(Pageable.class);
        when(dealsRepository.findAll(pageable)).thenReturn(mockPage);

        // When
        Page<Deal> result = dealsService.getDeals(pageable);

        // Then
        assertEquals(mockPage, result);
    }

//    @Test
//    void createDeal_ShouldReturnCreatedDeal() {
//        // Given
//        Deal inputDeal = new Deal();
//        Deal expectedDeal = new Deal();
//        when(dealsRepository.save(inputDeal)).thenReturn(expectedDeal);
//
//        // When
//        Deal result = dealsService.createDeal(inputDeal);
//
//        // Then
//        assertEquals(expectedDeal, result);
//    }
//
//    @Test
//    void updateDeal_WhenDealExists_ShouldReturnUpdatedDeal() throws DealException {
//        // Given
//        ObjectId dealId = new ObjectId("6035c09333d5390c6a618f4a");
//        Deal existingDeal = new Deal();
//        Deal updatedDeal = new Deal();
//        updatedDeal.setTitle("Updated Title");
//
//        when(dealsRepository.findById(dealId)).thenReturn(Optional.of(existingDeal));
//        when(dealsRepository.save(existingDeal)).thenReturn(updatedDeal);
//
//        // When
//        Deal result = dealsService.updateDeal(dealId, updatedDeal);
//
//        // Then
//        assertEquals(updatedDeal, result);
//        assertEquals("Updated Title", result.getTitle());
//    }
//
//    @Test
//    void updateDeal_WhenDealDoesNotExist_ShouldThrowException() {
//        // Given
//        ObjectId dealId = new ObjectId("6035c09333d5390c6a618f4a");
//        Deal updatedDeal = new Deal();
//        when(dealsRepository.findById(dealId)).thenReturn(Optional.empty());
//
//        // When, Then
//        assertThrows(DealException.class, () -> dealsService.updateDeal(dealId, updatedDeal));
//    }

    @Test
    void deleteDeal_WhenDealExists_ShouldDeleteDeal() {
        // Given
        ObjectId dealId = new ObjectId("6035c09333d5390c6a618f4a");
        when(dealsRepository.existsById(dealId)).thenReturn(true);

        // When
        assertDoesNotThrow(() -> dealsService.deleteDeal(dealId));

        // Then: No exception should be thrown
        verify(dealsRepository).deleteById(dealId);
    }

    @Test
    void deleteDeal_WhenDealDoesNotExist_ShouldThrowException() {
        // Given
        ObjectId dealId = new ObjectId("6035c09333d5390c6a618f4a");
        when(dealsRepository.existsById(dealId)).thenReturn(false);

        // When, Then
        assertThrows(DealException.class, () -> dealsService.deleteDeal(dealId));
    }

    @Test
    void getDealById_WhenDealExists_ShouldReturnDeal() throws DealException {
        // Given
        ObjectId dealId = new ObjectId("6035c09333d5390c6a618f4a");
        Deal expectedDeal = new Deal();
        when(dealsRepository.findById(dealId)).thenReturn(Optional.of(expectedDeal));

        // When
        Deal result = dealsService.getDealById(dealId);

        // Then
        assertEquals(expectedDeal, result);
    }

    @Test
    void getDealById_WhenDealDoesNotExist_ShouldThrowException() {
        // Given
        ObjectId dealId = new ObjectId("6035c09333d5390c6a618f4a");
        when(dealsRepository.findById(dealId)).thenReturn(Optional.empty());

        // When, Then
        assertThrows(DealException.class, () -> dealsService.getDealById(dealId));
    }

    @Test
    void searchDeals_ShouldReturnDealsPage() {
        // Given
        String searchText = "example";

        when(mongoTemplate.count(any(Query.class), eq(Deal.class))).thenReturn(1L);
        when(mongoTemplate.find(any(Query.class), eq(Deal.class)))
                .thenReturn(new ArrayList<>());

        // When
        Page<Deal> actualPage = dealsService.searchDeals(searchText, Pageable.unpaged());

        // Then
        assertNotNull(actualPage);
        assertEquals(0, actualPage.getContent().size());
    }
}
