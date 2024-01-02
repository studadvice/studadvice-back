package com.stud.studadvice.controller;

import com.stud.studadvice.dto.FaqDto;
import com.stud.studadvice.entity.Faq;
import com.stud.studadvice.exception.FaqException;
import com.stud.studadvice.exception.ImageException;
import com.stud.studadvice.service.FaqService;

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

@RestController
@RequestMapping("/faq")
public class FaqController {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FaqService faqService;

    @Operation(summary = "Retrieve a list of all question and response combinations")
    @GetMapping
    public Page<FaqDto> getAllQuestionAndResponseCombinations(
            @RequestParam(defaultValue = "${spring.data.web.pageable.default-page}") int page,
            @RequestParam(defaultValue = "${spring.data.web.pageable.default-page-size}") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return faqService.getAllQuestionAndResponseCombinations(pageable);
    }

    @Operation(summary = "Retrieve a question and response combination by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Question and response combination retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Question and response combination not found"),
    })
    @GetMapping("/{id}")
    public FaqDto getQuestionAndResponseCombinationById(@PathVariable ObjectId id) {
        try {
            return faqService.getQuestionAndResponseCombinationById(id);
        } catch (FaqException faqException) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, faqException.getMessage(), faqException);
        }
    }

    @Operation(summary = "Create a new question and response combination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Question and response combination created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request - Invalid input"),
    })
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE }, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN')")
    public FaqDto createQuestionAndResponseCombination(@Valid @RequestPart FaqDto faq, @RequestParam("imageFile") MultipartFile imageFile) {
        try {
            Faq newFaq = modelMapper.map(faq, Faq.class);
            return faqService.createQuestionAndResponseCombination(newFaq, imageFile);
        } catch (ImageException imageException) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, imageException.getMessage(), imageException);
        }
    }

    @Operation(summary = "Update an existing question and response combination by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Question and response combination updated successfully"),
            @ApiResponse(responseCode = "404", description = "Question and response combination not found"),
    })
    @PutMapping(value = "/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE }, produces = MediaType.APPLICATION_JSON_VALUE)
    public FaqDto updateQuestionAndResponseCombination(@PathVariable ObjectId id, @Valid @RequestPart FaqDto updatedFaqDto, @RequestParam("imageFile") MultipartFile imageFile) {
        try {
            Faq updatedFaq = modelMapper.map(updatedFaqDto, Faq.class);
            return faqService.updateQuestionAndResponseCombination(id, updatedFaq, imageFile);
        } catch (FaqException | ImageException faqException) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, faqException.getMessage(), faqException);
        }
    }

    @Operation(summary = "Delete a question and response combination by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Question and response combination deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Question and response combination not found"),
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteQuestionAndResponseCombination(@PathVariable ObjectId id) {
        try {
            faqService.deleteQuestionAndResponseCombination(id);
        } catch (FaqException faqException) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, faqException.getMessage(), faqException);
        }
    }

    @Operation(summary = "Search for question and response combinations by text")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Question and response combinations retrieved successfully"),
    })
    @GetMapping("/search")
    public Page<FaqDto> searchQuestionAndResponseCombinations(@RequestParam(required = false) String searchText,
                                                              @RequestParam(defaultValue = "${spring.data.web.pageable.default-page}") int page,
                                                              @RequestParam(defaultValue = "${spring.data.web.pageable.default-page-size}") int size){

        Pageable pageable = PageRequest.of(page, size);
        return faqService.searchQuestionAndResponseCombinations(searchText, pageable);
    }
}
