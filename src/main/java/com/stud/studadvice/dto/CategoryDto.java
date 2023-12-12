package com.stud.studadvice.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.util.List;

@Getter
@Setter
public class CategoryDto {
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
    @NotNull(message = "Please, give a name for your category")
    private String name;
    @NotNull(message = "Please, give a description for your category")
    private String description;
    @NotNull(message = "Please, give an image id for your category")
    private String imageId;
    @NotNull(message = "Please, give a color for your category")
    private String color;
    private List<AdministrativeProcessDto> administrativeProcesses;
    public CategoryDto() {
    }

}
