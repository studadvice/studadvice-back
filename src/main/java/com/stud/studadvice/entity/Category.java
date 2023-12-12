package com.stud.studadvice.entity;

import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

import org.bson.types.ObjectId;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;

@Getter
@Setter
@Document
public class Category {
    @Id
    private ObjectId id;
    @NotNull(message = "Please, give a name for your category")
    private String name;
    @NotNull(message = "Please, give a description for your category")
    private String description;
    @NotNull(message = "Please, give an image id for your category")
    private String imageId;
    @NotNull(message = "Please, give a color for your category")
    private String color;
    @DocumentReference
    private List<AdministrativeProcess> administrativeProcesses;
    public Category() {
    }

}
