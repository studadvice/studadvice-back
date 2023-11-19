package com.stud.studadvice.controller;

import com.stud.studadvice.exception.AdministrativeProcessException;
import com.stud.studadvice.model.administrative.AdministrativeProcess;
import com.stud.studadvice.service.AdministrativeProcessService;

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
@RequestMapping("/administrative-process")
public class AdministrativeProcessController {

    @Autowired
    private AdministrativeProcessService administrativeProcessService;

    /**
     * Retrieves an administrative process by its unique ID.
     *
     * @param administrativeProcessId The ID of the administrative process to retrieve.
     * @return The administrative process with the specified ID.
     */
    @Operation(summary = "Retrieve an administrative process by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Administrative process retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Administrative process not found"),
    })
    @GetMapping("/{administrativeProcessId}")
    public AdministrativeProcess getAdministrativeProcessById(@PathVariable ObjectId administrativeProcessId) {
        try {
            return administrativeProcessService.getAdministrativeProcessById(administrativeProcessId);
        }
        catch (AdministrativeProcessException administrativeProcessException){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, administrativeProcessException.getMessage(), administrativeProcessException);
        }
    }

    /**
     * Creates a new administrative process.
     *
     * @param administrativeProcess The administrative process to be created.
     * @return The newly created administrative process.
     */
    @Operation(summary = "Create a new administrative process")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Administrative process created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request - Invalid input"),
    })
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public AdministrativeProcess createAdministrativeProcess(@RequestBody AdministrativeProcess administrativeProcess) {
        try {
            return administrativeProcessService.createAdministrativeProcess(administrativeProcess);
        }
        catch (AdministrativeProcessException administrativeProcessException){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, administrativeProcessException.getMessage(), administrativeProcessException);
        }
    }

    /**
     * Updates an existing administrative process.
     *
     * @param administrativeProcessId            The ID of the administrative process to update.
     * @param updatedProcess The updated administrative process data.
     * @return The updated administrative process.
     */
    @Operation(summary = "Update an existing administrative process")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Administrative process updated successfully"),
            @ApiResponse(responseCode = "404", description = "Administrative process not found"),
    })
    @PutMapping("/{administrativeProcessId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public AdministrativeProcess updateAdministrativeProcess(@PathVariable ObjectId administrativeProcessId, @RequestBody AdministrativeProcess updatedProcess) {
        try {
            return administrativeProcessService.updateAdministrativeProcess(administrativeProcessId,updatedProcess);
        }
        catch (AdministrativeProcessException administrativeProcessException){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, administrativeProcessException.getMessage(), administrativeProcessException);
        }
    }

    /**
     * Deletes an administrative process by its ID.
     *
     * @param administrativeProcessId The unique ID of the administrative process to delete.
     */
    @Operation(summary = "Delete an administrative process by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Administrative process deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Administrative process not found"),
    })
    @DeleteMapping("/{administrativeProcessId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteAdministrativeProcess(@PathVariable ObjectId administrativeProcessId) {
        try{
            administrativeProcessService.deleteAdministrativeProcess(administrativeProcessId);
        }
        catch (AdministrativeProcessException administrativeProcessException){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, administrativeProcessException.getMessage(), administrativeProcessException);
        }
    }


    /**
     * Retrieves administrative processes based on age, nationalities, and universities.
     *
     * @param minAge          The age criterion.
     * @param nationalities The list of nationalities criterion.
     * @param universities  The list of universities criterion.
     * @return A list of administrative processes based on the specified criteria.
     */
    @Operation(summary = "Retrieve administrative processes by age, nationalities, and universities")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Administrative processes retrieved successfully"),
    })
    @GetMapping
    public List<AdministrativeProcess> getAdministrativeProcesses(
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge,
            @RequestParam(required = false) List<String> nationalities,
            @RequestParam(required = false) List<String> universities) {
        return administrativeProcessService.getAdministrativeProcesses(minAge, maxAge, nationalities, universities);
    }

    /**
     * Searches for administrative processes based on a text search.
     *
     * @param searchText The text to search for in the administrative processes.
     * @return A list of administrative processes matching the search criteria.
     */
    @Operation(summary = "Search for administrative processes by text")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Administrative processes retrieved successfully"),
    })
    @GetMapping("/search")
    public List<AdministrativeProcess> searchAdministrativeProcess(@RequestParam String searchText){
        return administrativeProcessService.searchAdministrativeProcess(searchText);
    }

}
