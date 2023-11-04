package com.stud.studadvice.model.administrative;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document
public class AdministrativeProcess {
    @Field("_id")
    @Id
    private ObjectId _id;
    private Informations informations;
    private List<Step> stepList;
    private List<Resource> resourceList;
    private List<RequiredDocument> requiredDocumentsList;
    private Category category;

    public AdministrativeProcess() {
    }

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public Informations getInformations() {
        return informations;
    }

    public void setInformations(Informations informations) {
        this.informations = informations;
    }

    public List<Resource> getResourceList() {
        return resourceList;
    }

    public void setResourceList(List<Resource> resourceList) {
        this.resourceList = resourceList;
    }

    public List<Step> getStepList() {
        return stepList;
    }

    public void setStepList(List<Step> stepList) {
        this.stepList = stepList;
    }

    public List<RequiredDocument> getRequiredDocumentsList() {
        return requiredDocumentsList;
    }

    public void setRequiredDocumentsList(List<RequiredDocument> requiredDocumentsList) {
        this.requiredDocumentsList = requiredDocumentsList;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
