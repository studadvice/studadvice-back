package com.stud.studadvice.service;

import com.stud.studadvice.exception.AdministrativeProcessException;
import com.stud.studadvice.model.administrative.AdministrativeProcess;
import com.stud.studadvice.repository.administrative.AdministrativeProcessRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdministrativeProcessService {
    @Autowired
    private AdministrativeProcessRepository administrativeProcessRepository;

    public List<String> getAdministrativeProcessCategories() {
        return administrativeProcessRepository.findDistinctCategories();
    }

    public List<String> getCategorySubCategories(String category) {
        return administrativeProcessRepository.findDistinctSubCategoriesByCategory(category);
    }

    public List<AdministrativeProcess> getAdministrativeProcess(String category, String subCategory) {
        return administrativeProcessRepository.findByCategoryAndSubCategory(category, subCategory);
    }

    public AdministrativeProcess getAdministrativeProcessById(ObjectId id) throws AdministrativeProcessException {
        AdministrativeProcess process = administrativeProcessRepository.findById(id).orElse(null);
        if (process == null) {
            throw new AdministrativeProcessException("Administrative process not found");
        }
        return process;
    }

    public AdministrativeProcess createAdministrativeProcess(AdministrativeProcess administrativeProcess) {
        return administrativeProcessRepository.save(administrativeProcess);
    }

    public AdministrativeProcess updateAdministrativeProcess(ObjectId id, AdministrativeProcess updatedProcess) throws AdministrativeProcessException {
        AdministrativeProcess existingProcess = administrativeProcessRepository.findById(id).orElse(null);
        if (existingProcess == null) {
            throw new AdministrativeProcessException("Administrative process to update not found");
        }

        existingProcess.setInformations(updatedProcess.getInformations());
        existingProcess.setResourceList(updatedProcess.getResourceList());
        existingProcess.setStepList(updatedProcess.getStepList());
        existingProcess.setRequiredDocumentsList(updatedProcess.getRequiredDocumentsList());
        existingProcess.setCategory(updatedProcess.getCategory());

        return administrativeProcessRepository.save(existingProcess);
    }

    public void deleteAdministrativeProcess(ObjectId id) throws AdministrativeProcessException {
        if (!administrativeProcessRepository.existsById(id)) {
            throw new AdministrativeProcessException("Administrative process to delete not found");
        }
        administrativeProcessRepository.deleteById(id);
    }
}
