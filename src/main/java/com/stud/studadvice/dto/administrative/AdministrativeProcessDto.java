package com.stud.studadvice.dto.administrative;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.util.List;

@Getter
@Setter
public class AdministrativeProcessDto {
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
    @NotNull(message = "Please, give a name for your administrative process")
    private String name;
    @NotNull(message = "Please, give a description for your administrative process")
    private String description;
    private String imageId;
    private List<String> educations;
    private Integer minAge;
    private Integer maxAge;
    private List<String> nationalities;
    private List<String> universities;
    private List<Step> steps;

    public AdministrativeProcessDto() {
    }
}
