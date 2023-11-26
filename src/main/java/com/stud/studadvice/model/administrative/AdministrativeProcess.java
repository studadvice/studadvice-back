package com.stud.studadvice.model.administrative;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import jakarta.validation.constraints.NotNull;

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
    @NotNull(message = "Please, give a name for your administrative process")
    @TextIndexed
    private String name;
    @NotNull(message = "Please, give a description for your administrative process")
    @TextIndexed
    private String description;
    @NotNull(message = "Please, give an image for your administrative process")
    private String image;
    private List<String> educations;
    private Integer minAge;
    private Integer maxAge;
    private List<String> nationalities;
    private List<String> universities;
    private List<Step> steps;

    public AdministrativeProcess() {
    }
}
