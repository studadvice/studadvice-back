package com.stud.studadvice.controller;

import com.stud.studadvice.service.AdministrativeProcessService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class AdministrativeProcessControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Mock
    private AdministrativeProcessService administrativeProcessService;

    @BeforeEach
    public void setUp() {
    }

    @Test
    void givenExistentAdministrativeProcessId_whenGetAdministrativeProcessById_thenReturnStatusOK() {
        // TODO
    }

    @Test
    void givenInexistentAdministrativeProcessId_whenGetAdministrativeProcessById_thenReturnStatusNotFound() {
        // TODO
    }

    @Test
    void givenValidInput_whenCreateAdministrativeProcess_thenReturnStatusCreated() {
        // TODO
    }

    @Test
    void givenValidInputWithNoImage_whenCreateAdministrativeProcess_thenReturnStatusBadRequest() {
        // TODO
    }

    @Test
    void givenInvalidInput_whenCreateAdministrativeProcess_thenReturnStatusBadRequest() {
        // TODO
    }

    @Test
    void givenExistentAdministrativeProcessIdAndValidInput_whenUpdateAdministrativeProcess_thenReturnStatusOK() {
        // TODO
    }

    @Test
    void givenAdministrativeProcessIdAndInvalidInput_whenUpdateAdministrativeProcess_thenReturnStatusNotFound() {
        // TODO
    }

    @Test
    void givenExistentAdministrativeProcessId_whenDeleteAdministrativeProcess_thenReturnStatusNoContent() {
        // TODO
    }

    @Test
    void givenInexistentAdministrativeProcessId_whenDeleteAdministrativeProcess_thenReturnStatusNoContent() {
        // TODO
    }

    @Test
    void givenSearchParams_whenGetAdministrativeProcesses_thenReturnStatusOK() {
        // TODO
    }

    @Test
    void givenSearchText_whenSearchAdministrativeProcess_thenReturnStatusOK() {
        // TODO
    }

    @Test
    void givenSearchParamsWithNoFilter_whenGetAdministrativeProcesses_thenStatusOK() {
        // TODO
    }

    @Test
    void givenSearchText_whenSearchAdministrativeProcess_thenStatusOK() {
        // TODO
    }

    @Test
    void givenEmptySearchText_whenSearchAdministrativeProcess_thenStatusOK() {
        // TODO
    }
}
