package com.stud.studadvice.entity;

import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

import org.bson.types.ObjectId;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;

@Getter
@Setter
@Document
public class AdministrativeProcess {
    @Id
    private ObjectId id;
    @NotNull(message = "Please, give a name for your administrative process")
    @TextIndexed
    private String name;
    @NotNull(message = "Please, give a description for your administrative process")
    @TextIndexed
    private String description;
    @NotNull(message = "Please, give an image id for your administrative process")
    private String imageId;
    private List<String> educations;
    private Integer minAge;
    private Integer maxAge;
    private List<String> nationalities;
    private List<String> universities;
    private List<Step> steps;
    private String category;
    private String startDate;
    private String endDate;
    public AdministrativeProcess() {
    }
}
