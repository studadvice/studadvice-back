package com.stud.studadvice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StepDto {
    @NotNull(message = "Please, give a step number of your step")
    private int stepNumber;
    @NotNull(message = "Please, give a name for your step")
    private String name;
    @NotNull(message = "Please, give a description for your step")
    private String description;
    private String imageId;
    private List<RequiredDocumentDto> requiredDocuments;
    private List<ResourceDto> resources;

    public StepDto() {
    }
}
