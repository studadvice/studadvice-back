package com.stud.studadvice.controller;

import com.stud.studadvice.exception.RequiredDocumentException;
import com.stud.studadvice.model.administrative.RequiredDocument;
import com.stud.studadvice.model.deal.Deal;
import com.stud.studadvice.service.RequiredDocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/required-document")
public class RequiredDocumentController {

    @Autowired
    private RequiredDocumentService requiredDocumentService;

    @Operation(summary = "Retrieve all required documents")
    @GetMapping
    public Page<RequiredDocument> getRequiredDocuments(@RequestParam(defaultValue = "${spring.data.web.pageable.default-page}") int page,
                                                        @RequestParam(defaultValue = "${spring.data.web.pageable.default-page-size}") int size){

        Pageable pageable = PageRequest.of(page, size);
        return requiredDocumentService.getRequiredDocuments(pageable);
    }

    @Operation(summary = "Retrieve a required document by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Required document retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Required document process not found"),
    })
    @GetMapping("/{requiredDocumentId}")
    public RequiredDocument getRequiredDocumentById(@PathVariable ObjectId requiredDocumentId) {
        try {
            return requiredDocumentService.getRequiredDocumentById(requiredDocumentId);
        }
        catch (RequiredDocumentException requiredDocumentException){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, requiredDocumentException.getMessage(), requiredDocumentException);
        }
    }

    @Operation(summary = "Create a new required document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Required document created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request - Invalid input"),
    })
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public RequiredDocument createRequiredDocument(@Valid @RequestBody RequiredDocument requiredDocument) {
        return requiredDocumentService.createRequiredDocument(requiredDocument);
    }

    @Operation(summary = "Update an existing required document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Required document updated successfully"),
            @ApiResponse(responseCode = "404", description = "Required document not found"),
    })
    @PutMapping("/{requiredDocumentId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public RequiredDocument updateRequiredDocument(@PathVariable ObjectId requiredDocumentId, @Valid @RequestBody RequiredDocument requiredDocumentUpdated) {
        try {
            return requiredDocumentService.updateRequiredDocument(requiredDocumentId,requiredDocumentUpdated);
        }
        catch (RequiredDocumentException requiredDocumentException){
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
        try{
            requiredDocumentService.deleteRequiredDocument(requiredDocumentId);
        }
        catch (RequiredDocumentException requiredDocumentException){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, requiredDocumentException.getMessage(), requiredDocumentException);
        }
    }

    @Operation(summary = "Search for required documents by text")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Required documents retrieved successfully"),
    })
    @GetMapping("/search")
    public Page<RequiredDocument> searchRequiredDocuments(@RequestParam String searchText,
                                                          @RequestParam(defaultValue = "${spring.data.web.pageable.default-page}")int page,
                                                          @RequestParam(defaultValue = "${spring.data.web.pageable.default-page-size}") int size){

        Pageable pageable = PageRequest.of(page, size);
        return requiredDocumentService.searchRequiredDocuments(searchText,pageable);
    }

}
