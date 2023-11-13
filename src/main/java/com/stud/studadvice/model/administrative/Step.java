package com.stud.studadvice.model.administrative;

import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;

public class Step {
    private int stepNumber;
    private Informations informations;
    @DocumentReference
    private List<RequiredDocument> requiredDocumentList;

    public Step() {
    }

    public int getStepNumber() {
        return stepNumber;
    }

    public void setStepNumber(int stepNumber) {
        this.stepNumber = stepNumber;
    }

    public Informations getInformations() {
        return informations;
    }

    public void setInformations(Informations informations) {
        this.informations = informations;
    }

    public List<RequiredDocument> getRequiredDocumentList() {
        return requiredDocumentList;
    }

    public void setRequiredDocumentList(List<RequiredDocument> requiredDocumentList) {
        this.requiredDocumentList = requiredDocumentList;
    }
}
