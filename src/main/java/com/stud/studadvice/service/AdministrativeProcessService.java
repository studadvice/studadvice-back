package com.stud.studadvice.service;

import com.mongodb.client.gridfs.model.GridFSUploadOptions;

import com.stud.studadvice.exception.AdministrativeProcessException;
import com.stud.studadvice.exception.ImageException;
import com.stud.studadvice.model.administrative.AdministrativeProcess;
import com.stud.studadvice.model.administrative.RequiredDocument;
import com.stud.studadvice.model.administrative.Step;
import com.stud.studadvice.repository.administrative.AdministrativeProcessRepository;
import com.stud.studadvice.repository.administrative.RequiredDocumentRepository;

import org.bson.Document;
import org.bson.types.ObjectId;

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

import java.util.List;
import java.util.Objects;

@Service
public class AdministrativeProcessService {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private RequiredDocumentRepository requiredDocumentRepository;
    @Autowired
    private AdministrativeProcessRepository administrativeProcessRepository;
    @Autowired
    private GridFsTemplate gridFsTemplate;

    public AdministrativeProcess getAdministrativeProcessById(ObjectId administrativeProcessId) throws AdministrativeProcessException {
        return administrativeProcessRepository.findById(administrativeProcessId)
                .orElseThrow(() -> new AdministrativeProcessException("Administrative process not found"));
    }

    public AdministrativeProcess createAdministrativeProcess(AdministrativeProcess administrativeProcess, MultipartFile imageFile) throws AdministrativeProcessException, ImageException {
        if(administrativeProcess.getSteps()!= null) {
            for (Step step : administrativeProcess.getSteps()) {
                if(step.getRequiredDocuments()!= null) {
                    for (RequiredDocument requiredDocument : step.getRequiredDocuments()) {
                        if (requiredDocumentRepository.findById(requiredDocument.getId()).isEmpty()) {
                            throw new AdministrativeProcessException("Administrative process use a undefined required document. Please create it first");
                        }
                    }
                }
            }
        }

        try{
            String imageId = storeImage(imageFile);
            administrativeProcess.setImageId(imageId);
            return administrativeProcessRepository.save(administrativeProcess);

        }
        catch (IOException ioException){
            throw new ImageException("Error when storing the image");
        }
    }

    public AdministrativeProcess updateAdministrativeProcess(ObjectId administrativeProcessId, AdministrativeProcess updatedProcess,MultipartFile imageFile) throws AdministrativeProcessException, ImageException {

        AdministrativeProcess existingProcess = administrativeProcessRepository.findById(administrativeProcessId)
                .orElseThrow(() -> new AdministrativeProcessException("Administrative process not found"));

        if(updatedProcess.getSteps() != null) {
            for (Step step : updatedProcess.getSteps()) {
                if(step.getRequiredDocuments()!= null) {
                    for (RequiredDocument requiredDocument : step.getRequiredDocuments()) {
                        if (requiredDocumentRepository.findById(requiredDocument.getId()).isEmpty()) {
                            throw new AdministrativeProcessException("Administrative process use a undefined required document. Please create it first");
                        }
                    }
                }
            }
        }

        existingProcess.setDescription(updatedProcess.getDescription());
        existingProcess.setMaxAge(updatedProcess.getMaxAge());
        existingProcess.setMinAge(updatedProcess.getMinAge());
        existingProcess.setNationalities(updatedProcess.getNationalities());
        existingProcess.setUniversities(updatedProcess.getUniversities());
        existingProcess.setSteps(updatedProcess.getSteps());
        existingProcess.setName(updatedProcess.getName());

        try{
            String imageId = storeImage(imageFile);
            existingProcess.setImageId(imageId);
            return administrativeProcessRepository.save(existingProcess);
        }
        catch (IOException ioException){
            throw new ImageException("Error when storing the image");
        }
    }

    public void deleteAdministrativeProcess(ObjectId administrativeProcessId) throws AdministrativeProcessException {
        if (!administrativeProcessRepository.existsById(administrativeProcessId)) {
            throw new AdministrativeProcessException("Administrative process to delete not found");
        }
        administrativeProcessRepository.deleteById(administrativeProcessId);
    }

    public Page<AdministrativeProcess> getAdministrativeProcesses(Integer age, String nationality, String university, String education, Pageable pageable) {
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

        return PageableExecutionUtils.getPage(processes, pageable, () -> total);
    }

    public Page<AdministrativeProcess> searchAdministrativeProcess(String searchText,Pageable pageable) {
        TextCriteria criteria = TextCriteria.forDefaultLanguage()
                .matching(searchText);

        Query query = TextQuery.queryText(criteria)
                .sortByScore();

        query.with(pageable);

        long total = mongoTemplate.count(query, AdministrativeProcess.class);

        List<AdministrativeProcess> processes = mongoTemplate.find(query, AdministrativeProcess.class);

        return PageableExecutionUtils.getPage(processes, pageable, () -> total);
    }

    public String storeImage(MultipartFile imageFile) throws IOException {
        GridFSUploadOptions options = new GridFSUploadOptions()
                .metadata(new Document("contentType", imageFile.getContentType())
                        .append("contentSize", imageFile.getSize()));
        return gridFsTemplate.store(imageFile.getInputStream(), Objects.requireNonNull(imageFile.getOriginalFilename()),options).toString();
    }
}
