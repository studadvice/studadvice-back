package com.stud.studadvice.service;

import com.stud.studadvice.exception.AdministrativeProcessException;
import com.stud.studadvice.model.administrative.AdministrativeProcess;
import com.stud.studadvice.model.administrative.RequiredDocument;
import com.stud.studadvice.model.administrative.Step;
import com.stud.studadvice.repository.administrative.AdministrativeProcessRepository;

import com.stud.studadvice.repository.administrative.RequiredDocumentRepository;
import org.bson.types.ObjectId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdministrativeProcessService {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private RequiredDocumentRepository requiredDocumentRepository;

    @Autowired
    private AdministrativeProcessRepository administrativeProcessRepository;
    public AdministrativeProcess getAdministrativeProcessById(ObjectId administrativeProcessId) throws AdministrativeProcessException {
        return administrativeProcessRepository.findById(administrativeProcessId)
                .orElseThrow(() -> new AdministrativeProcessException("Administrative process not found"));
    }

    public AdministrativeProcess createAdministrativeProcess(AdministrativeProcess administrativeProcess) throws AdministrativeProcessException {

        for (Step step: administrativeProcess.getSteps()){
            for (RequiredDocument requiredDocument: step.getRequiredDocuments()) {
                requiredDocumentRepository.findById(requiredDocument.getId())
                        .orElseThrow(() -> new AdministrativeProcessException("Administrative process use a undefined required document. Please create it first"));
            }
        }

        return administrativeProcessRepository.save(administrativeProcess);
    }

    public AdministrativeProcess updateAdministrativeProcess(ObjectId administrativeProcessId, AdministrativeProcess updatedProcess) throws AdministrativeProcessException {

        AdministrativeProcess existingProcess = administrativeProcessRepository.findById(administrativeProcessId)
                .orElseThrow(() -> new AdministrativeProcessException("Administrative process not found"));


        for (Step step: updatedProcess.getSteps()){
            for (RequiredDocument requiredDocument: step.getRequiredDocuments()) {
                requiredDocumentRepository.findById(requiredDocument.getId())
                        .orElseThrow(() -> new AdministrativeProcessException("Administrative process use a undefined required document. Please create it first"));
            }
        }

        existingProcess.setImage(updatedProcess.getImage());
        existingProcess.setDescription(updatedProcess.getDescription());
        existingProcess.setMaxAge(updatedProcess.getMaxAge());
        existingProcess.setMinAge(updatedProcess.getMinAge());
        existingProcess.setNationalities(updatedProcess.getNationalities());
        existingProcess.setUniversities(updatedProcess.getUniversities());
        existingProcess.setResources(updatedProcess.getResources());
        existingProcess.setSteps(updatedProcess.getSteps());
        existingProcess.setName(updatedProcess.getName());

        return administrativeProcessRepository.save(existingProcess);
    }

    public void deleteAdministrativeProcess(ObjectId administrativeProcessId) throws AdministrativeProcessException {
        if (!administrativeProcessRepository.existsById(administrativeProcessId)) {
            throw new AdministrativeProcessException("Administrative process to delete not found");
        }
        administrativeProcessRepository.deleteById(administrativeProcessId);
    }

    public Page<AdministrativeProcess> getAdministrativeProcesses(Integer minAge, Integer maxAge, List<String> nationalities, List<String> universities, Pageable pageable) {
        Criteria criteria = new Criteria();

        if (minAge != null) {
            criteria.and("minAge").is(minAge);
        }

        if (maxAge != null) {
            criteria.and("maxAge").is(maxAge);
        }

        if (nationalities != null && !nationalities.isEmpty()) {
            criteria.and("nationalities").in(nationalities);
        }

        if (universities != null && !universities.isEmpty()) {
            criteria.and("universities").in(universities);
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
}
