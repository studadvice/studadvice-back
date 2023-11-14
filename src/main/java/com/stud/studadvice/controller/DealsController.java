package com.stud.studadvice.controller;

import com.stud.studadvice.exception.DealException;
import com.stud.studadvice.model.deal.Deal;
import com.stud.studadvice.service.DealsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.bson.types.ObjectId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Controller for managing student deals.
 */
@RestController
@RequestMapping("/deals")
public class DealsController {

    @Autowired
    private DealsService dealsService;

    /**
     * Retrieves a list of all student deals.
     *
     * @return A list of student deals.
     */
    @Operation(summary = "Retrieve a list of all student deals")
    @GetMapping
    public List<Deal> getDeals() {
        return dealsService.getAllDeals();
    }

    /**
     * Retrieves a deal by its ID.
     *
     * @param id The ID of the deal to retrieve.
     * @return The deals with the specified ID.
     */
    @Operation(summary = "Retrieve a deal by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deal retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Deal not found"),
    })
    @GetMapping("/{id}")
    public Deal getDealById(@PathVariable ObjectId id) {
        try {
            return dealsService.getDealById(id);
        } catch (DealException dealException) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, dealException.getMessage(), dealException);
        }
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
    public Deal createDeal(@RequestBody Deal newDeal) {
        return dealsService.createDeal(newDeal);
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
    public Deal updateDeal(@PathVariable ObjectId id, @RequestBody Deal updatedDeal) {
        try {
            return dealsService.updateDeal(id, updatedDeal);
        } catch (DealException dealException) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, dealException.getMessage(), dealException);
        }
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
    public void deleteDeal(@PathVariable ObjectId id) {
        try {
            dealsService.deleteDeal(id);
        } catch (DealException dealException) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, dealException.getMessage(), dealException);
        }
    }
}