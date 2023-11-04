package com.stud.studadvice.controller;

import com.stud.studadvice.model.deals.Deals;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing student deals.
 */
@RestController
@RequestMapping("/deals")
public class DealsController {

    /**
     * Retrieves a list of all student deals.
     *
     * @return A list of student deals.
     */
    @Operation(summary = "Retrieve a list of all student deals")
    @GetMapping
    public List<Deals> getDeals() {
        return null;
    }

    /**
     * Creates a new student deal.
     *
     * @param newDeal The student deal to be created.
     * @return The newly created student deal.
     */
    @Operation(summary = "Create a new student deal")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Student deal created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request - Invalid input"),
    })
    @PostMapping
    public Deals createDeal(@RequestBody Deals newDeal) {
        return null;
    }

    /**
     * Updates an existing student deal.
     *
     * @param id           The unique ID of the student deal to update.
     * @param updatedDeal The updated student deal data.
     * @return The updated student deal.
     */
    @Operation(summary = "Update an existing student deal by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student deal updated successfully"),
            @ApiResponse(responseCode = "404", description = "Student deal not found"),
    })
    @PutMapping("/{id}")
    public Deals updateDeal(@PathVariable String id, @RequestBody Deals updatedDeal) {
        return null;
    }

    /**
     * Deletes a student deal by its unique ID.
     *
     * @param id The unique ID of the student deal to delete.
     */
    @Operation(summary = "Delete a student deal by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Student deal deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Student deal not found"),
    })
    @DeleteMapping("/{id}")
    public void deleteDeal(@PathVariable String id) {
    }
}