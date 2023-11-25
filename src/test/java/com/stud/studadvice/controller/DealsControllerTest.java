package com.stud.studadvice.controller;

import com.stud.studadvice.exception.DealException;
import com.stud.studadvice.model.deal.Deal;
import com.stud.studadvice.service.DealsService;

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

public class DealsControllerTest {
    @Mock
    private DealsService dealsService;

    @InjectMocks
    private DealsController dealsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getDeals_WhenDealsExist_ShouldReturnDealsPage() {
        // Given
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        Page expectedPage = mock(Page.class);
        when(dealsService.getDeals(pageable)).thenReturn(expectedPage);

        // When
        Page<Deal> actualPage = dealsController.getDeals(page, size);

        // Then
        assertNotNull(actualPage);
        assertEquals(expectedPage, actualPage);
    }

    @Test
    void getDeals_WhenNoDealsExist_ShouldReturnEmptyPage() {
        // Given
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        Page<Deal> expectedPage = Page.empty();
        when(dealsService.getDeals(pageable)).thenReturn(expectedPage);

        // When
        Page<Deal> actualPage = dealsController.getDeals(page, size);

        // Then
        assertNotNull(actualPage);
        assertTrue(actualPage.isEmpty());
    }

    @Test
    void getDealById_WhenDealExists_ShouldReturnDeal() throws DealException {
        // Given
        ObjectId dealId = new ObjectId("6035c09333d5390c6a618f4a");
        Deal expectedDeal = new Deal();
        when(dealsService.getDealById(dealId)).thenReturn(expectedDeal);

        // When
        Deal actualDeal = dealsController.getDealById(dealId);

        // Then
        assertNotNull(actualDeal);
        assertEquals(expectedDeal, actualDeal);
    }

    @Test
    void getDealById_WhenDealDoesNotExist_ShouldThrowNotFoundException() throws DealException {
        // Given
        ObjectId dealId = new ObjectId("6035c09333d5390c6a618f4a");
        when(dealsService.getDealById(dealId))
                .thenThrow(new DealException("Deal not found"));

        // When, Then
        assertThrows(ResponseStatusException.class,
                () -> dealsController.getDealById(dealId),
                "Expected NotFoundException");
    }

    @Test
    void createDeal_WhenValidDeal_ShouldReturnCreatedDeal() {
        // Given
        Deal newDeal = new Deal();
        Deal expectedDeal = new Deal();
        when(dealsService.createDeal(newDeal)).thenReturn(expectedDeal);

        // When
        Deal actualDeal = dealsController.createDeal(newDeal);

        // Then
        assertNotNull(actualDeal);
        assertEquals(expectedDeal, actualDeal);
    }

    @Test
    void createDeal_WhenInvalidDeal_ShouldThrowBadRequestException() {
        // Given
        Deal newDeal = new Deal();

        when(dealsService.createDeal(newDeal))
                .thenThrow(new RuntimeException("Bad Request - Invalid input"));

        // When, Then
        assertThrows(RuntimeException.class,
                () -> dealsController.createDeal(newDeal),
                "Expected BadRequestException");
    }

    @Test
    void updateDeal_WhenValidDeal_ShouldReturnUpdatedDeal() throws DealException {
        // Given
        ObjectId dealId = new ObjectId("6035c09333d5390c6a618f4a");
        Deal updatedDeal = new Deal();
        Deal expectedDeal = new Deal();
        when(dealsService.updateDeal(dealId, updatedDeal)).thenReturn(expectedDeal);

        // When
        Deal actualDeal = dealsController.updateDeal(dealId, updatedDeal);

        // Then
        assertNotNull(actualDeal);
        assertEquals(expectedDeal, actualDeal);
    }

    @Test
    void updateDeal_WhenDealDoesNotExist_ShouldReturnNotFoundException() throws DealException {
        // Given
        ObjectId dealId = new ObjectId("6035c09333d5390c6a618f4a");
        Deal updatedDeal = new Deal();
        when(dealsService.updateDeal(dealId, updatedDeal))
                .thenThrow(new DealException("Student deal not found"));

        // When, Then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> dealsController.updateDeal(dealId, updatedDeal));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Student deal not found", exception.getReason());
    }

    @Test
    void deleteDeal_WhenDealExists_ShouldDeleteDeal() throws DealException {
        // Given
        ObjectId dealId = new ObjectId("6035c09333d5390c6a618f4a");

        // When
        assertDoesNotThrow(() -> dealsController.deleteDeal(dealId));

        // Then: No exception should be thrown
        verify(dealsService).deleteDeal(dealId);
    }

    @Test
    void deleteDeal_WhenDealDoesNotExist_ShouldReturnNotFoundException() throws DealException {
        // Given
        ObjectId dealId = new ObjectId("6035c09333d5390c6a618f4a");
        doThrow(new DealException("Student deal not found"))
                .when(dealsService).deleteDeal(dealId);

        // When, Then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> dealsController.deleteDeal(dealId));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Student deal not found", exception.getReason());
    }

    @Test
    void searchDeals_WhenResultsExist_ShouldReturnResultsPage() {
        // Given
        String searchText = "example";
        int page = 0;
        int size = 10;

        Pageable pageable = PageRequest.of(page, size);
        Page expectedPage = mock(Page.class);
        when(dealsService.searchDeals(searchText, pageable))
                .thenReturn(expectedPage);

        // When
        Page<Deal> actualPage = dealsController.searchDeals(searchText, page, size);

        // Then
        assertNotNull(actualPage);
        assertEquals(expectedPage, actualPage);
    }

    @Test
    void searchDeals_WhenNoResultsExist_ShouldReturnEmptyPage() {
        // Given
        String searchText = "nonexistent";
        int page = 0;
        int size = 10;

        Pageable pageable = PageRequest.of(page, size);
        Page<Deal> expectedPage = Page.empty();
        when(dealsService.searchDeals(searchText, pageable))
                .thenReturn(expectedPage);

        // When
        Page<Deal> actualPage = dealsController.searchDeals(searchText, page, size);

        // Then
        assertNotNull(actualPage);
        assertTrue(actualPage.isEmpty());
    }
}
