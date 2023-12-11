package com.stud.studadvice.controller;

import com.stud.studadvice.service.DealsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class DealsControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private DealsService dealsService;

    @BeforeEach
    public void setUp() {
    }

    @Test
    void givenExistingDealId_whenGetDealsById_thenReturnStatusOK() {
        // TODO
    }

    @Test
    void givenInexistingDealsId_whenGetDealsById_thenReturnStatusNotFound() {
        // TODO
    }

    @Test
    void givenValidInput_whenCreateDeals_thenReturnStatusCreated() {
        // TODO
    }

    @Test
    void givenValidInputWithNoImage_whenCreateDeals_thenReturnStatusBadRequest() {
        // TODO
    }

    @Test
    void givenInvalidInput_whenCreateDeals_thenReturnStatusBadRequest() {
        // TODO
    }

    @Test
    void givenDealsIdAndValidInput_whenUpdateDeals_thenReturnStatusOK() {
        // TODO
    }

    @Test
    void givenDealsIdAndInvalidInput_whenUpdateDeals_thenReturnStatusNotFound() {
        // TODO
    }

    @Test
    void givenExistentDealId_whenDeleteDeals_thenReturnStatusNoContent() {
        // TODO
    }

    @Test
    void givenInexistentDealsId_whenDeleteDeals_thenReturnStatusNoContent() {
    // TODO
   }

    @Test
    void givenSearchText_whenSearchDeals_thenStatusOK() {
        // TODO
    }

    @Test
    void givenEmptySearchText_whenSearchDeals_thenStatusOK() {
        // TODO
    }

    @Test
    void givenSearchTextWithSpecialCharacters_whenSearchDeals_thenStatusOK() {
        // TODO
    }

    @Test
    void givenSearchParams_whenSearchDeals_thenStatusOK() {
        // TODO
    }

    @Test
    void givenSearchParamsWithNoFilter_whenSearchDeals_thenStatusOK() {
        // TODO
    }

}
