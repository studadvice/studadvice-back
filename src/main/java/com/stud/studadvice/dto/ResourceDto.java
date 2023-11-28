package com.stud.studadvice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceDto {
    @NotNull(message = "Please, give a name for your resource")
    private String name;
    private String url;
    private String image;
    @NotNull(message = "Please, give a description for your resource")
    private String description;

    public ResourceDto() {
    }
}
