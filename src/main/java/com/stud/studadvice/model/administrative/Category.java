package com.stud.studadvice.model.administrative;

import org.bson.types.ObjectId;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document
public class Category {
    @Field("_id")
    @Id
    private ObjectId id;
    private Informations informations;
    @DocumentReference
    private List<AdministrativeProcess> administrativeProcesses;
    public Category() {
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public Informations getInformations() {
        return informations;
    }

    public void setInformations(Informations informations) {
        this.informations = informations;
    }

    public List<AdministrativeProcess> getAdministrativeProcesses() {
        return administrativeProcesses;
    }

    public void setAdministrativeProcesses(List<AdministrativeProcess> administrativeProcesses) {
        this.administrativeProcesses = administrativeProcesses;
    }

}
