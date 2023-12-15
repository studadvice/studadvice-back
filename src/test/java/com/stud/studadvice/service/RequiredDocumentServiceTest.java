package com.stud.studadvice.service;

import com.stud.studadvice.dto.RequiredDocumentDto;
import com.stud.studadvice.entity.RequiredDocument;
import com.stud.studadvice.exception.ImageException;
import com.stud.studadvice.exception.RequiredDocumentException;
import com.stud.studadvice.repository.administrative.RequiredDocumentRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class RequiredDocumentServiceTest {

    @Mock
    private GridFsTemplate gridFsTemplate;

    @Mock
    private RequiredDocumentRepository requiredDocumentRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private RequiredDocumentService requiredDocumentService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetRequiredDocuments_ShouldReturnPageOfRequiredDocumentDto() {
        Pageable pageable = Pageable.unpaged();
        List<RequiredDocument> documentList = Collections.singletonList(new RequiredDocument());
        Page<RequiredDocument> page = new PageImpl<>(documentList);
        when(requiredDocumentRepository.findAll(pageable)).thenReturn(page);

        List<RequiredDocumentDto> dtoList = Collections.singletonList(new RequiredDocumentDto());
        when(modelMapper.map(any(RequiredDocument.class), eq(RequiredDocumentDto.class)))
                .thenReturn(new RequiredDocumentDto());

        Page<RequiredDocumentDto> result = requiredDocumentService.getRequiredDocuments(pageable);

        assertEquals(page.getTotalElements(), result.getTotalElements());
        assertEquals(dtoList.size(), result.getContent().size());
        verify(requiredDocumentRepository, times(1)).findAll(pageable);
        verify(modelMapper, times(dtoList.size())).map(any(RequiredDocument.class), eq(RequiredDocumentDto.class));
    }


    @Test
    void testGetRequiredDocumentById_ExistingId_ShouldReturnDto() throws RequiredDocumentException {
        // Arrange
        ObjectId id = new ObjectId();
        RequiredDocument requiredDocument = new RequiredDocument();
        when(requiredDocumentRepository.findById(id)).thenReturn(Optional.of(requiredDocument));

        RequiredDocumentDto dto = new RequiredDocumentDto();
        when(modelMapper.map(requiredDocument, RequiredDocumentDto.class)).thenReturn(dto);

        // Act
        RequiredDocumentDto result = requiredDocumentService.getRequiredDocumentById(id);

        // Assert
        assertEquals(dto, result);
        verify(requiredDocumentRepository, times(1)).findById(id);
        verify(modelMapper, times(1)).map(requiredDocument, RequiredDocumentDto.class);
    }

    @Test
    void testGetRequiredDocumentById_NonExistingId_ShouldThrowException() {
        ObjectId id = new ObjectId();
        when(requiredDocumentRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RequiredDocumentException.class, () -> requiredDocumentService.getRequiredDocumentById(id));

        verify(requiredDocumentRepository, times(1)).findById(id);
        verify(modelMapper, never()).map(any(), eq(RequiredDocumentDto.class));
    }

    @Test
    void testDeleteRequiredDocument_ExistingId_ShouldDeleteDocument() throws RequiredDocumentException {
        ObjectId id = new ObjectId("6035c992be21c22b821eb366");
        RequiredDocument existingDocument = new RequiredDocument();
        existingDocument.setId(id);

        when(requiredDocumentRepository.findById(id)).thenReturn(Optional.of(existingDocument));

        requiredDocumentService.deleteRequiredDocument(id);

        verify(requiredDocumentRepository, times(1)).delete(existingDocument);
    }

    @Test
    void testDeleteRequiredDocument_NonExistingId_ShouldThrowException() {
        ObjectId id = new ObjectId("6035c992be21c22b821eb366");

        when(requiredDocumentRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RequiredDocumentException.class, () -> {
            requiredDocumentService.deleteRequiredDocument(id);
        });

        verify(requiredDocumentRepository, never()).delete(any());
    }

}
