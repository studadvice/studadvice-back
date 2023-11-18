package com.stud.studadvice.controller;

import com.stud.studadvice.exception.RequiredDocumentException;
import com.stud.studadvice.model.administrative.RequiredDocument;
import com.stud.studadvice.service.RequiredDocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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
    private List<RequiredDocument> getRequiredDocument(){
        return requiredDocumentService.getRequiredDocument();
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
    @PreAuthorize("hasAuthority('ADMIN')")
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
    @PreAuthorize("hasAuthority('ADMIN')")
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
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteRequiredDocument(@PathVariable ObjectId requiredDocumentId) {
        try{
            requiredDocumentService.deleteRequiredDocument(requiredDocumentId);
        }
        catch (RequiredDocumentException requiredDocumentException){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, requiredDocumentException.getMessage(), requiredDocumentException);
        }
    }
}