package com.stud.studadvice.model.administrative;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Getter;
import lombok.Setter;

import org.bson.types.ObjectId;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@Document
public class AdministrativeProcess {
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
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
