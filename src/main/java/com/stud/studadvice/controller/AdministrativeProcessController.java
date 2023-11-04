package com.stud.studadvice.controller;

import com.stud.studadvice.model.administrative.AdministrativeProcess;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/administrative-process")
public class AdministrativeProcessController {

    /**
     * Retrieves all categories of administrative processes.
     *
     * @return A list of administrative process categories.
     */
    @Operation(summary = "Retrieve all administrative process categories")
    @GetMapping("/categories")
    private List<String> getAdministrativeProcessCategories(){
        return null;
    }

    /**
     * Retrieves sub-categories for a given category.
     *
     * @param category The category for which to retrieve sub-categories.
     * @return A list of sub-categories for the specified category.
     */
    @Operation(summary = "Retrieve sub-categories for a specific category")
    @GetMapping("/categories/{category}/sub-categories")
    private List<String> getCategorySubCategories(@PathVariable String category){
        return null;
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
        return null;
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
    public AdministrativeProcess getAdministrativeProcessById(@PathVariable String id) {
        return null;
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
    @PostMapping("/")
    public AdministrativeProcess createAdministrativeProcess(@RequestBody AdministrativeProcess administrativeProcess) {
        return null;
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
    public AdministrativeProcess updateAdministrativeProcess(@PathVariable String id, @RequestBody AdministrativeProcess updatedProcess) {
        return null;
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
    public void deleteAdministrativeProcess(@PathVariable String id) {
    }

}
