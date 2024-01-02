package com.stud.studadvice.service;

import com.stud.studadvice.dto.DealDto;
import com.stud.studadvice.entity.Deal;
import com.stud.studadvice.exception.DealException;
import com.stud.studadvice.repository.deals.DealsRepository;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.modelmapper.ModelMapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class DealsServiceTest {

    @Mock
    private DealsRepository dealRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private DealsService dealService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDeleteDeal_ExistingId_ShouldNotThrowException() {
        ObjectId dealId = new ObjectId();

        when(dealRepository.existsById(any())).thenReturn(true);

        assertDoesNotThrow(() -> dealService.deleteDeal(dealId));

        verify(dealRepository, times(1)).existsById(dealId);
        verify(dealRepository, times(1)).deleteById(dealId);
    }

    @Test
    void testDeleteDeal_NonExistingId_ShouldThrowException() {
        ObjectId dealId = new ObjectId();

        when(dealRepository.existsById(any())).thenReturn(false);

        DealException exception = assertThrows(DealException.class,
                () -> dealService.deleteDeal(dealId));

        assertEquals("Student deal not found", exception.getMessage());

        verify(dealRepository, times(1)).existsById(dealId);
        verify(dealRepository, never()).deleteById(dealId);
    }

    @Test
    void testGetDealById_ExistingId_ShouldReturnDto() throws DealException {
        ObjectId dealId = new ObjectId();
        Deal deal = new Deal();
        DealDto dealDto = new DealDto();

        when(dealRepository.findById(dealId)).thenReturn(Optional.of(deal));
        when(modelMapper.map(deal, DealDto.class)).thenReturn(dealDto);

        DealDto resultDto = dealService.getDealById(dealId);

        assertNotNull(resultDto);
        assertSame(dealDto, resultDto);

        verify(dealRepository, times(1)).findById(dealId);
        verify(modelMapper, times(1)).map(deal, DealDto.class);
    }

    @Test
    void testGetDealById_NonExistingId_ShouldThrowException() {
        ObjectId dealId = new ObjectId();

        when(dealRepository.findById(dealId)).thenReturn(Optional.empty());

        DealException exception = assertThrows(DealException.class,
                () -> dealService.getDealById(dealId));

        assertEquals("Student deal not found", exception.getMessage());

        verify(dealRepository, times(1)).findById(dealId);
        verify(modelMapper, never()).map(any(), eq(DealDto.class));
    }

}
