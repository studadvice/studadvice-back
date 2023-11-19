package com.stud.studadvice.controller;

import com.stud.studadvice.exception.DealException;
import com.stud.studadvice.model.deal.Deal;
import com.stud.studadvice.service.DealsService;

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
    public Page<Deal> getDeals(@RequestParam(defaultValue = "${spring.data.web.pageable.default-page}") int page,
                               @RequestParam(defaultValue = "${spring.data.web.pageable.default-page-size}") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return dealsService.getDeals(pageable);
    }

    /**
     * Retrieves a deal by its ID.
     *
     * @param dealId The ID of the deal to retrieve.
     * @return The deals with the specified ID.
     */
    @Operation(summary = "Retrieve a deal by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deal retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Deal not found"),
    })
    @GetMapping("/{dealId}")
    public Deal getDealById(@PathVariable ObjectId dealId) {
        try {
            return dealsService.getDealById(dealId);
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
     * @param dealId           The unique ID of the student deal to update.
     * @param updatedDeal The updated student deal data.
     * @return The updated student deal.
     */
    @Operation(summary = "Update an existing student deal by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student deal updated successfully"),
            @ApiResponse(responseCode = "404", description = "Student deal not found"),
    })
    @PutMapping("/{dealId}")
    public Deal updateDeal(@PathVariable ObjectId dealId, @RequestBody Deal updatedDeal) {
        try {
            return dealsService.updateDeal(dealId, updatedDeal);
        } catch (DealException dealException) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, dealException.getMessage(), dealException);
        }
    }

    /**
     * Deletes a student deal by its unique ID.
     *
     * @param dealId The unique ID of the student deal to delete.
     */
    @Operation(summary = "Delete a student deal by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Student deal deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Student deal not found"),
    })
    @DeleteMapping("/{dealId}")
    public void deleteDeal(@PathVariable ObjectId dealId) {
        try {
            dealsService.deleteDeal(dealId);
        } catch (DealException dealException) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, dealException.getMessage(), dealException);
        }
    }

    /**
     * Searches for deals based on a text search.
     *
     * @param searchText The text to search for in the deals.
     * @return A list of the deals matching the search criteria.
     */
    @Operation(summary = "Search for deals by text")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deals retrieved successfully"),
    })
    @GetMapping("/search")
    public Page<Deal> searchDeals(@RequestParam String searchText,
                                  @RequestParam(defaultValue = "${spring.data.web.pageable.default-page}") int page,
                                  @RequestParam(defaultValue = "${spring.data.web.pageable.default-page-size}") int size){

        Pageable pageable = PageRequest.of(page, size);
        return dealsService.searchDeals(searchText,pageable);
    }
}