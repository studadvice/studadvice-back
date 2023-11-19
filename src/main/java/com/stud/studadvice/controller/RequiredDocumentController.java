package com.stud.studadvice.controller;

import com.stud.studadvice.exception.RequiredDocumentException;
import com.stud.studadvice.model.administrative.RequiredDocument;
import com.stud.studadvice.model.deal.Deal;
import com.stud.studadvice.service.RequiredDocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/required-document")
public class RequiredDocumentController {

    @Autowired
    private RequiredDocumentService requiredDocumentService;

    /**
     * Retrieves all the required document.
     * @return A list of required documents.
     */
    @Operation(summary = "Retrieve all required documents")
    @GetMapping
    private Page<RequiredDocument> getRequiredDocuments(@RequestParam(defaultValue = "${spring.data.web.pageable.default-page}") int page,
                                                        @RequestParam(defaultValue = "${spring.data.web.pageable.default-page-size}") int size){

        Pageable pageable = PageRequest.of(page, size);
        return requiredDocumentService.getRequiredDocuments(pageable);
    }

    /**
     * Retrieves a required document by its ID.
     *
     * @param requiredDocumentId The ID of the required document to retrieve.
     * @return The required document with the specified ID.
     */
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

    /**
     * Creates a new required document .
     *
     * @param requiredDocument The required document to be created.
     * @return The newly created required document.
     */
    @Operation(summary = "Create a new required document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Required document created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request - Invalid input"),
    })
    @PostMapping
    public RequiredDocument createRequiredDocument(@RequestBody RequiredDocument requiredDocument) {
        return requiredDocumentService.createRequiredDocument(requiredDocument);
    }

    /**
     * Updates an existing required document.
     *
     * @param requiredDocumentId            The ID of the required document to update.
     * @param requiredDocumentUpdated The updated required document data.
     * @return The updated required document.
     */
    @Operation(summary = "Update an existing required document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Required document updated successfully"),
            @ApiResponse(responseCode = "404", description = "Required document not found"),
    })
    @PutMapping("/{requiredDocumentId}")
    public RequiredDocument updateRequiredDocument(@PathVariable ObjectId requiredDocumentId, @RequestBody RequiredDocument requiredDocumentUpdated) {
        try {
            return requiredDocumentService.updateRequiredDocument(requiredDocumentId,requiredDocumentUpdated);
        }
        catch (RequiredDocumentException requiredDocumentException){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, requiredDocumentException.getMessage(), requiredDocumentException);
        }
    }

    /**
     * Deletes a required document by its ID.
     *
     * @param requiredDocumentId The unique ID of the required document to delete.
     */
    @Operation(summary = "Delete a required document process by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Required document deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Required document not found"),
    })
    @DeleteMapping("/{requiredDocumentId}")
    public void deleteRequiredDocument(@PathVariable ObjectId requiredDocumentId) {
        try{
            requiredDocumentService.deleteRequiredDocument(requiredDocumentId);
        }
        catch (RequiredDocumentException requiredDocumentException){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, requiredDocumentException.getMessage(), requiredDocumentException);
        }
    }

    /**
     * Searches for required documents based on a text search.
     *
     * @param searchText The text to search for in the required documents.
     * @return A list of the required documents matching the search criteria.
     */
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
