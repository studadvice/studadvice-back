package com.stud.studadvice.model.administrative;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Getter
@Setter
@Document
public class AdministrativeProcess {
    @Field("_id")
    @Id
    private ObjectId id;
    @TextIndexed
    private String name;
    @TextIndexed
    private String description;
    private String image;
    private Integer minAge;
    private Integer maxAge;
    private List<String> nationalities;
    private List<String> universities;
    private List<Step> steps;
    private List<Resource> resources;

    public AdministrativeProcess() {
    }
}
