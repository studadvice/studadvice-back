package com.stud.studadvice.controller;

import com.stud.studadvice.dto.DealDto;
import com.stud.studadvice.entity.AdministrativeProcess;
import com.stud.studadvice.exception.DealException;
import com.stud.studadvice.exception.ImageException;
import com.stud.studadvice.entity.Deal;
import com.stud.studadvice.service.DealsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

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


/**
 * Controller for managing student deals.
 */
@RestController
@RequestMapping("/deals")
public class DealsController {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private DealsService dealsService;

    @Operation(summary = "Retrieve a list of all student deals")
    @GetMapping
    public Page<DealDto> getDeals(@RequestParam(defaultValue = "${spring.data.web.pageable.default-page}") int page,
                                  @RequestParam(defaultValue = "${spring.data.web.pageable.default-page-size}") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return dealsService.getDeals(pageable);
    }

    @Operation(summary = "Retrieve a deal by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deal retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Deal not found"),
    })
    @GetMapping("/{dealId}")
    public DealDto getDealById(@PathVariable ObjectId dealId) {
        try {
            return dealsService.getDealById(dealId);
        } catch (DealException dealException) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, dealException.getMessage(), dealException);
        }
    }


    @Operation(summary = "Create a new student deal")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Student deal created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request - Invalid input"),
    })
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE }, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN')")
    public DealDto createDeal(@Valid @RequestPart DealDto deal,@RequestParam("imageFile") MultipartFile imageFile) {
        try{
            Deal newDeal = modelMapper.map(deal, Deal.class);
            return dealsService.createDeal(newDeal,imageFile);
        }
        catch (ImageException imageException){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, imageException.getMessage(), imageException);
        }
    }

    @Operation(summary = "Update an existing student deal by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student deal updated successfully"),
            @ApiResponse(responseCode = "404", description = "Student deal not found"),
    })
    @PutMapping(value = "/{dealId}",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE }, produces = MediaType.APPLICATION_JSON_VALUE)
    public DealDto updateDeal(@PathVariable ObjectId dealId, @Valid @RequestPart DealDto deal,@RequestParam("imageFile") MultipartFile imageFile) {
        try {
            Deal updatedDeal = modelMapper.map(deal, Deal.class);
            return dealsService.updateDeal(dealId, updatedDeal,imageFile);
        } catch (DealException | ImageException dealException) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, dealException.getMessage(), dealException);
        }
    }

    @Operation(summary = "Delete a student deal by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Student deal deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Student deal not found"),
    })
    @DeleteMapping("/{dealId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteDeal(@PathVariable ObjectId dealId) {
        try {
            dealsService.deleteDeal(dealId);
        } catch (DealException dealException) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, dealException.getMessage(), dealException);
        }
    }

    @Operation(summary = "Search for deals by text")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deals retrieved successfully"),
    })
    @GetMapping("/search")
    public Page<DealDto> searchDeals(@RequestParam String searchText,
                                  @RequestParam(defaultValue = "${spring.data.web.pageable.default-page}") int page,
                                  @RequestParam(defaultValue = "${spring.data.web.pageable.default-page-size}") int size){

        Pageable pageable = PageRequest.of(page, size);
        return dealsService.searchDeals(searchText,pageable);
    }
}
