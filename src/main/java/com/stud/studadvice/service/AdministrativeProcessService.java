package com.stud.studadvice.service;

import com.stud.studadvice.exception.AdministrativeProcessException;
import com.stud.studadvice.model.administrative.AdministrativeProcess;
import com.stud.studadvice.model.administrative.RequiredDocument;
import com.stud.studadvice.model.administrative.Step;
import com.stud.studadvice.repository.administrative.AdministrativeProcessRepository;

import com.stud.studadvice.repository.administrative.RequiredDocumentRepository;
import org.bson.types.ObjectId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdministrativeProcessService {
    @Autowired
    private RequiredDocumentRepository requiredDocumentRepository;

    @Autowired
    private AdministrativeProcessRepository administrativeProcessRepository;

    public List<AdministrativeProcess> getAdministrativeProcess() {
        return administrativeProcessRepository.findAll();
    }

    public AdministrativeProcess getAdministrativeProcessById(ObjectId administrativeProcessId) throws AdministrativeProcessException {
        return administrativeProcessRepository.findById(administrativeProcessId)
                .orElseThrow(() -> new AdministrativeProcessException("Administrative process not found"));
    }

    public AdministrativeProcess createAdministrativeProcess(AdministrativeProcess administrativeProcess) throws AdministrativeProcessException {

        for (Step step: administrativeProcess.getStepList()){
            for (RequiredDocument requiredDocument: step.getRequiredDocumentList()) {
                requiredDocumentRepository.findById(requiredDocument.getId())
                        .orElseThrow(() -> new AdministrativeProcessException("Administrative process use a undefined required document. Please create it first"));
            }
        }

        return administrativeProcessRepository.save(administrativeProcess);
    }

    public AdministrativeProcess updateAdministrativeProcess(ObjectId administrativeProcessId, AdministrativeProcess updatedProcess) throws AdministrativeProcessException {

        AdministrativeProcess existingProcess = administrativeProcessRepository.findById(administrativeProcessId)
                .orElseThrow(() -> new AdministrativeProcessException("Administrative process not found"));


        for (Step step: updatedProcess.getStepList()){
            for (RequiredDocument requiredDocument: step.getRequiredDocumentList()) {
                requiredDocumentRepository.findById(requiredDocument.getId())
                        .orElseThrow(() -> new AdministrativeProcessException("Administrative process use a undefined required document. Please create it first"));
            }
        }

        existingProcess.setInformations(updatedProcess.getInformations());
        existingProcess.setResourceList(updatedProcess.getResourceList());
        existingProcess.setStepList(updatedProcess.getStepList());

        return administrativeProcessRepository.save(existingProcess);
    }

    public void deleteAdministrativeProcess(ObjectId administrativeProcessId) throws AdministrativeProcessException {
        if (!administrativeProcessRepository.existsById(administrativeProcessId)) {
            throw new AdministrativeProcessException("Administrative process to delete not found");
        }
        administrativeProcessRepository.deleteById(administrativeProcessId);
    }
}
