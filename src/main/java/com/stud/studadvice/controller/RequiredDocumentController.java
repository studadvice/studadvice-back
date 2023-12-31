package com.stud.studadvice.controller;

import com.stud.studadvice.dto.RequiredDocumentDto;
import com.stud.studadvice.entity.RequiredDocument;
import com.stud.studadvice.exception.ImageException;
import com.stud.studadvice.exception.RequiredDocumentException;
import com.stud.studadvice.entity.RequiredDocument;
import com.stud.studadvice.service.RequiredDocumentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;

import org.bson.types.ObjectId;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/required-document")
public class RequiredDocumentController {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private RequiredDocumentService requiredDocumentService;

    @Operation(summary = "Retrieve all required documents")
    @GetMapping
    public Page<RequiredDocumentDto> getRequiredDocuments(@RequestParam(defaultValue = "${spring.data.web.pageable.default-page}") int page,
                                                          @RequestParam(defaultValue = "${spring.data.web.pageable.default-page-size}") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return requiredDocumentService.getRequiredDocuments(pageable);
    }

    @Operation(summary = "Retrieve a required document by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Required document retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Required document process not found"),
    })
    @GetMapping("/{requiredDocumentId}")
    public RequiredDocumentDto getRequiredDocumentById(@PathVariable ObjectId requiredDocumentId) {
        try {
            return requiredDocumentService.getRequiredDocumentById(requiredDocumentId);
        } catch (RequiredDocumentException requiredDocumentException) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, requiredDocumentException.getMessage(), requiredDocumentException);
        }
    }

    @Operation(summary = "Create a new required document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Required document created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request - Invalid input"),
    })
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN')")
    public RequiredDocumentDto createRequiredDocument(@Valid @RequestPart RequiredDocumentDto requiredDocument, @Nullable @RequestParam("imageFile") MultipartFile imageFile) {
        try {
            RequiredDocument requiredDocumentToCreate = modelMapper.map(requiredDocument, RequiredDocument.class);
            return requiredDocumentService.createRequiredDocument(requiredDocumentToCreate, imageFile);
        } catch (ImageException imageException) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, imageException.getMessage(), imageException);
        }
    }

    @Operation(summary = "Update an existing required document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Required document updated successfully"),
            @ApiResponse(responseCode = "404", description = "Required document not found"),
    })
    @PutMapping(value = "/{requiredDocumentId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    public RequiredDocumentDto updateRequiredDocument(@PathVariable ObjectId requiredDocumentId, @Valid @RequestPart RequiredDocumentDto requiredDocument, @Nullable @RequestParam("imageFile") MultipartFile imageFile) {
        try {
            RequiredDocument requiredDocumentToUpdate = modelMapper.map(requiredDocument, RequiredDocument.class);
            return requiredDocumentService.updateRequiredDocument(requiredDocumentId, requiredDocumentToUpdate, imageFile);
        } catch (RequiredDocumentException | ImageException requiredDocumentException) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, requiredDocumentException.getMessage(), requiredDocumentException);
        }
    }

    @Operation(summary = "Delete a required document process by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Required document deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Required document not found"),
    })
    @DeleteMapping("/{requiredDocumentId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteRequiredDocument(@PathVariable ObjectId requiredDocumentId) {
        try {
            requiredDocumentService.deleteRequiredDocument(requiredDocumentId);
        } catch (RequiredDocumentException requiredDocumentException) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, requiredDocumentException.getMessage(), requiredDocumentException);
        }
    }

    @Operation(summary = "Search for required documents by text")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Required documents retrieved successfully"),
    })
    @GetMapping("/search")
    public Page<RequiredDocumentDto> searchRequiredDocuments(@RequestParam String searchText,
                                                             @RequestParam(defaultValue = "${spring.data.web.pageable.default-page}") int page,
                                                             @RequestParam(defaultValue = "${spring.data.web.pageable.default-page-size}") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return requiredDocumentService.searchRequiredDocuments(searchText, pageable);
    }
}
