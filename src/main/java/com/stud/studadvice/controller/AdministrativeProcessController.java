package com.stud.studadvice.controller;

import com.stud.studadvice.dto.AdministrativeProcessDto;
import com.stud.studadvice.dto.CategoryDto;
import com.stud.studadvice.exception.AdministrativeProcessException;
import com.stud.studadvice.exception.ImageException;
import com.stud.studadvice.entity.AdministrativeProcess;
import com.stud.studadvice.service.AdministrativeProcessService;

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
@RequestMapping("/administrative-process")
@CrossOrigin(origins = "http://studadvice.com")
public class AdministrativeProcessController {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private AdministrativeProcessService administrativeProcessService;

    @Operation(summary = "Retrieve an administrative process by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Administrative process retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Administrative process not found"),
    })
    @GetMapping("/{administrativeProcessId}")
    public AdministrativeProcessDto getAdministrativeProcessById(@PathVariable ObjectId administrativeProcessId) {
        try {
            return administrativeProcessService.getAdministrativeProcessById(administrativeProcessId);
        }
        catch (AdministrativeProcessException administrativeProcessException){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, administrativeProcessException.getMessage(), administrativeProcessException);
        }
    }

    @Operation(summary = "Create a new administrative process")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Administrative process created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request - Invalid input"),
    })
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN')")
    public AdministrativeProcessDto createAdministrativeProcess(@Valid @RequestPart("administrativeProcess") AdministrativeProcessDto administrativeProcess, @RequestParam("imageFile") MultipartFile imageFile) {
        try {
            AdministrativeProcess administrativeProcessToCreate = modelMapper.map(administrativeProcess, AdministrativeProcess.class);
            return administrativeProcessService.createAdministrativeProcess(administrativeProcessToCreate, imageFile);
        } catch (AdministrativeProcessException | ImageException administrativeProcessException) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, administrativeProcessException.getMessage(), administrativeProcessException);
        }
    }

    @Operation(summary = "Update an existing administrative process")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Administrative process updated successfully"),
            @ApiResponse(responseCode = "404", description = "Administrative process not found"),
    })
    @PutMapping(value = "/{administrativeProcessId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN')")
    public AdministrativeProcessDto updateAdministrativeProcess(@PathVariable ObjectId administrativeProcessId, @Valid @RequestPart AdministrativeProcessDto administrativeProcess, @RequestParam("imageFile") MultipartFile imageFile) {
        try {
            AdministrativeProcess updatedProcess = modelMapper.map(administrativeProcess, AdministrativeProcess.class);
            return administrativeProcessService.updateAdministrativeProcess(administrativeProcessId, updatedProcess, imageFile);
        } catch (AdministrativeProcessException | ImageException administrativeProcessException) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, administrativeProcessException.getMessage(), administrativeProcessException);
        }
    }


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


    @Operation(summary = "Retrieve administrative processes by age, nationalities, and universities")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Administrative processes retrieved successfully"),
    })
    @GetMapping
    public Page<AdministrativeProcessDto> getAdministrativeProcesses(
            @RequestParam(required = false) Integer age,
            @RequestParam(required = false) String nationality,
            @RequestParam(required = false) String university,
            @RequestParam(required = false) String education,
            @RequestParam(defaultValue = "${spring.data.web.pageable.default-page}") int page,
            @RequestParam(defaultValue = "${spring.data.web.pageable.default-page-size}") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return administrativeProcessService.getAdministrativeProcesses(age, nationality, university,education,pageable);
    }

    @Operation(summary = "Search for administrative processes by text")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Administrative processes retrieved successfully"),
    })
    @GetMapping("/search")
    public Page<AdministrativeProcessDto> searchAdministrativeProcess(@RequestParam(required = false) ObjectId categoryId,@RequestParam(required = false) String searchText,
                                                         @RequestParam(defaultValue = "${spring.data.web.pageable.default-page}") int page, @RequestParam(defaultValue = "${spring.data.web.pageable.default-page-size}") int size){
        Pageable pageable = PageRequest.of(page, size);
        return administrativeProcessService.searchAdministrativeProcess(categoryId,searchText,pageable);
    }

}
