package com.stud.studadvice.service;

import com.mongodb.client.gridfs.model.GridFSUploadOptions;

import com.stud.studadvice.dto.AdministrativeProcessDto;
import com.stud.studadvice.exception.AdministrativeProcessException;
import com.stud.studadvice.exception.ImageException;
import com.stud.studadvice.entity.AdministrativeProcess;
import com.stud.studadvice.entity.RequiredDocument;
import com.stud.studadvice.entity.Step;
import com.stud.studadvice.repository.administrative.AdministrativeProcessRepository;
import com.stud.studadvice.repository.administrative.RequiredDocumentRepository;

import org.bson.Document;
import org.bson.types.ObjectId;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdministrativeProcessService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private RequiredDocumentRepository requiredDocumentRepository;
    @Autowired
    private AdministrativeProcessRepository administrativeProcessRepository;
    @Autowired
    private GridFsTemplate gridFsTemplate;

    public AdministrativeProcessDto getAdministrativeProcessById(ObjectId administrativeProcessId) throws AdministrativeProcessException {
        AdministrativeProcess administrativeProcess = administrativeProcessRepository.findById(administrativeProcessId)
                .orElseThrow(() -> new AdministrativeProcessException("Administrative process not found"));
        return modelMapper.map(administrativeProcess, AdministrativeProcessDto.class);
    }

    public AdministrativeProcessDto createAdministrativeProcess(AdministrativeProcess administrativeProcess, MultipartFile imageFile)
            throws AdministrativeProcessException, ImageException {
        validateRequiredDocuments(administrativeProcess);

        try {
            String imageId = storeImage(imageFile);
            administrativeProcess.setImageId(imageId);
            AdministrativeProcess administrativeProcessCreated = administrativeProcessRepository.save(administrativeProcess);
            return modelMapper.map(administrativeProcessCreated, AdministrativeProcessDto.class);
        } catch (IOException ioException) {
            throw new ImageException("Error when storing the image");
        }
    }

    void validateRequiredDocuments(AdministrativeProcess administrativeProcess) throws AdministrativeProcessException {
        if (administrativeProcess.getSteps() != null) {
            for (Step step : administrativeProcess.getSteps()) {
                if (step.getRequiredDocuments() != null) {
                    for (RequiredDocument requiredDocument : step.getRequiredDocuments()) {
                        if (requiredDocumentRepository.findById(requiredDocument.getId()).isEmpty()) {
                            throw new AdministrativeProcessException("Administrative process uses an undefined required document. Please create it first");
                        }
                    }
                }
            }
        }
    }

    public AdministrativeProcessDto updateAdministrativeProcess(ObjectId administrativeProcessId, AdministrativeProcess updatedProcess, MultipartFile imageFile)
            throws AdministrativeProcessException, ImageException {

        AdministrativeProcess existingProcess = administrativeProcessRepository.findById(administrativeProcessId)
                .orElseThrow(() -> new AdministrativeProcessException("Administrative process not found"));

        validateRequiredDocuments(updatedProcess);

        updateProcessFields(existingProcess, updatedProcess);

        try {
            String imageId = storeImage(imageFile);
            existingProcess.setImageId(imageId);

            AdministrativeProcess administrativeProcessUpdated = administrativeProcessRepository.save(existingProcess);
            return modelMapper.map(administrativeProcessUpdated, AdministrativeProcessDto.class);
        } catch (IOException ioException) {
            throw new ImageException("Error when storing the image");
        }
    }

    private void updateProcessFields(AdministrativeProcess existingProcess, AdministrativeProcess updatedProcess) {
        existingProcess.setDescription(updatedProcess.getDescription());
        existingProcess.setMaxAge(updatedProcess.getMaxAge());
        existingProcess.setMinAge(updatedProcess.getMinAge());
        existingProcess.setNationalities(updatedProcess.getNationalities());
        existingProcess.setUniversities(updatedProcess.getUniversities());
        existingProcess.setSteps(updatedProcess.getSteps());
        existingProcess.setName(updatedProcess.getName());
    }

    public void deleteAdministrativeProcess(ObjectId administrativeProcessId) throws AdministrativeProcessException {
        if (!administrativeProcessRepository.existsById(administrativeProcessId)) {
            throw new AdministrativeProcessException("Administrative process to delete not found");
        }
        administrativeProcessRepository.deleteById(administrativeProcessId);
    }

    public Page<AdministrativeProcessDto> getAdministrativeProcesses(Integer age, String nationality, String university, String education, Pageable pageable) {
        Criteria criteria = new Criteria();

        if (age != null) {
            criteria.and("minAge").lte(age).and("maxAge").gte(age);
        }

        if (nationality != null) {
            criteria.and("nationalities").in(nationality);
        }

        if (university != null) {
            criteria.and("universities").in(university);
        }

        if (education != null) {
            criteria.and("education").is(education);
        }

        Query query = new Query(criteria);

        long total = mongoTemplate.count(query, AdministrativeProcess.class);

        query.with(pageable);

        List<AdministrativeProcess> processes = mongoTemplate.find(query, AdministrativeProcess.class);

        List<AdministrativeProcessDto> dtos = processes.stream()
                .map(process -> modelMapper.map(process, AdministrativeProcessDto.class))
                .collect(Collectors.toList());

        return PageableExecutionUtils.getPage(dtos, pageable, () -> total);
    }


    public Page<AdministrativeProcessDto> searchAdministrativeProcess(ObjectId categoryId, String searchText, Pageable pageable) {
        if (categoryId != null) {
            return searchByCategoryId(categoryId, searchText, pageable);
        } else {
            return searchWithoutCategoryId(searchText, pageable);
        }
    }

    private Page<AdministrativeProcessDto> searchWithoutCategoryId(String searchText, Pageable pageable) {
        Query query = new Query();

        if (searchText != null && !searchText.isEmpty()) {
            TextCriteria criteria = TextCriteria.forDefaultLanguage().matching(searchText);
            query.addCriteria(criteria);
        }

        long total = mongoTemplate.count(query, AdministrativeProcess.class);

        query.with(pageable);

        List<AdministrativeProcess> processes = mongoTemplate.find(query, AdministrativeProcess.class);

        List<AdministrativeProcessDto> dtos = new ArrayList<>();

        for (AdministrativeProcess administrativeProcess : processes) {
            AdministrativeProcessDto dto = modelMapper.map(administrativeProcess, AdministrativeProcessDto.class);
            dtos.add(dto);
        }

        return PageableExecutionUtils.getPage(dtos, pageable, () -> total);
    }


    private Page<AdministrativeProcessDto> searchByCategoryId(ObjectId categoryId, String searchText, Pageable pageable) {
        Query query = new Query().addCriteria(Criteria.where("category").is(categoryId));

        if (searchText != null && !searchText.isEmpty()) {
            TextCriteria criteria = TextCriteria.forDefaultLanguage().matching(searchText);
            query.addCriteria(criteria);
        }

        long total = mongoTemplate.count(query, AdministrativeProcess.class);

        query.with(pageable);

        List<AdministrativeProcess> processes = mongoTemplate.find(query, AdministrativeProcess.class);

        List<AdministrativeProcessDto> dtos = new ArrayList<>();

        for (AdministrativeProcess administrativeProcess : processes) {
            AdministrativeProcessDto dto = modelMapper.map(administrativeProcess, AdministrativeProcessDto.class);
            dtos.add(dto);
        }

        return PageableExecutionUtils.getPage(dtos, pageable, () -> total);
    }



    public String storeImage(MultipartFile imageFile) throws IOException {
        GridFSUploadOptions options = new GridFSUploadOptions()
                .metadata(new Document("contentType", imageFile.getContentType())
                        .append("contentSize", imageFile.getSize()));
        return gridFsTemplate.store(imageFile.getInputStream(), Objects.requireNonNull(imageFile.getOriginalFilename()),options).toString();
    }
}
