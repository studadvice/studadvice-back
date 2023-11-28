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
public class Category {
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
    @NotNull(message = "Please, give a name for your category")
    private String name;
    @NotNull(message = "Please, give a description for your category")
    private String description;
    private String imageId;
    private List<AdministrativeProcessDto> administrativeProcesses;
    public Category() {
    }

}
