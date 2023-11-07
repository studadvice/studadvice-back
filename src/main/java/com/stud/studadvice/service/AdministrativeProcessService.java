package com.stud.studadvice.service;

import com.stud.studadvice.exception.AdministrativeProcessException;
import com.stud.studadvice.exception.CategoryException;
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

    public List<AdministrativeProcess> getAdministrativeProcess() {
        return administrativeProcessRepository.findAll();
    }

    public AdministrativeProcess getAdministrativeProcessById(ObjectId id) throws AdministrativeProcessException {
        AdministrativeProcess process = administrativeProcessRepository.findById(id).orElse(null);
        if (process == null) {
            throw new AdministrativeProcessException("Administrative process not found");
        }
        return process;
    }

    public AdministrativeProcess createAdministrativeProcess(AdministrativeProcess administrativeProcess) throws CategoryException{
        // TODO check if the requireddocuments exist
        return administrativeProcessRepository.save(administrativeProcess);
    }

    public AdministrativeProcess updateAdministrativeProcess(ObjectId id, AdministrativeProcess updatedProcess) throws AdministrativeProcessException {
        AdministrativeProcess existingProcess = administrativeProcessRepository.findById(id).orElse(null);
        if (existingProcess == null) {
            throw new AdministrativeProcessException("Administrative process to update not found");
        }

        // TODO check if the requireddocuments exist

        existingProcess.setInformations(updatedProcess.getInformations());
        existingProcess.setResourceList(updatedProcess.getResourceList());
        existingProcess.setStepList(updatedProcess.getStepList());
        existingProcess.setRequiredDocumentsList(updatedProcess.getRequiredDocumentsList());

        return administrativeProcessRepository.save(existingProcess);
    }

    public void deleteAdministrativeProcess(ObjectId id) throws AdministrativeProcessException {
        if (!administrativeProcessRepository.existsById(id)) {
            throw new AdministrativeProcessException("Administrative process to delete not found");
        }
        administrativeProcessRepository.deleteById(id);
    }
}
