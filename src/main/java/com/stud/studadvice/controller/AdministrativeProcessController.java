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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/administrative-process")
public class AdministrativeProcessController {
    @Autowired
    private AdministrativeProcessService administrativeProcessService;

    /**
     * Retrieves all categories of administrative processes.
     *
     * @return A list of administrative process categories.
     */
    @Operation(summary = "Retrieve all administrative process categories")
    @GetMapping("/categories")
    private List<String> getAdministrativeProcessCategories(){
        return administrativeProcessService.getAdministrativeProcessCategories();
    }

    /**
     * Retrieves sub-categories for a given category.
     *
     * @param category The category for which to retrieve sub-categories.
     * @return A list of sub-categories for the specified category.
     */
    @Operation(summary = "Retrieve sub-categories for a specific category")
    @GetMapping("/{category}/sub-categories")
    private List<String> getCategorySubCategories(@PathVariable String category){
        return administrativeProcessService.getCategorySubCategories(category);
    }

    /**
     * Retrieves administrative processes by category and sub-category.
     *
     * @param category    The category of the administrative process.
     * @param subCategory The sub-category of the administrative process.
     * @return A list of administrative processes based on the category and sub-category.
     */
    @Operation(summary = "Retrieve administrative processes by category and sub-category")
    @GetMapping
    private List<AdministrativeProcess> getAdministrativeProcess(@RequestParam String category, @RequestParam String subCategory){
        return administrativeProcessService.getAdministrativeProcess(category,subCategory);
    }

    /**
     * Retrieves an administrative process by its unique ID.
     *
     * @param id The ID of the administrative process to retrieve.
     * @return The administrative process with the specified ID.
     */
    @Operation(summary = "Retrieve an administrative process by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Administrative process retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Administrative process not found"),
    })
    @GetMapping("/{id}")
    public AdministrativeProcess getAdministrativeProcessById(@PathVariable ObjectId id) {
        try {
            return administrativeProcessService.getAdministrativeProcessById(id);
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
    public AdministrativeProcess createAdministrativeProcess(@RequestBody AdministrativeProcess administrativeProcess) {
        return administrativeProcessService.createAdministrativeProcess(administrativeProcess);
    }

    /**
     * Updates an existing administrative process.
     *
     * @param id            The ID of the administrative process to update.
     * @param updatedProcess The updated administrative process data.
     * @return The updated administrative process.
     */
    @Operation(summary = "Update an existing administrative process")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Administrative process updated successfully"),
            @ApiResponse(responseCode = "404", description = "Administrative process not found"),
    })
    @PutMapping("/{id}")
    public AdministrativeProcess updateAdministrativeProcess(@PathVariable ObjectId id, @RequestBody AdministrativeProcess updatedProcess) {
        try {
            return administrativeProcessService.updateAdministrativeProcess(id,updatedProcess);
        }
        catch (AdministrativeProcessException administrativeProcessException){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, administrativeProcessException.getMessage(), administrativeProcessException);
        }
    }

    /**
     * Deletes an administrative process by its ID.
     *
     * @param id The unique ID of the administrative process to delete.
     */
    @Operation(summary = "Delete an administrative process by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Administrative process deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Administrative process not found"),
    })
    @DeleteMapping("/{id}")
    public void deleteAdministrativeProcess(@PathVariable ObjectId id) {
        try{
            administrativeProcessService.deleteAdministrativeProcess(id);
        }
        catch (AdministrativeProcessException administrativeProcessException){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, administrativeProcessException.getMessage(), administrativeProcessException);
        }
    }

}
