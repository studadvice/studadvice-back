package com.stud.studadvice.model.administrative;

import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;

@Getter
@Setter
public class Step {
    @NotNull(message = "Please, give a step number of your step")
    private int stepNumber;
    @NotNull(message = "Please, give a name for your step")
    private String name;
    @NotNull(message = "Please, give a description for your step")
    private String description;
    private String imageId;
    @DocumentReference
    private List<RequiredDocument> requiredDocuments;
    private List<Resource> resources;

    public Step() {
    }
}
