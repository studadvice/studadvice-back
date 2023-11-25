package com.stud.studadvice.model.administrative;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document
public class RequiredDocument {
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
    @TextIndexed
    @NotNull(message = "Please, give a name for your required document")
    private String name;
    @TextIndexed
    @NotNull(message = "Please, give a description for your required document")
    private String description;
    private String image;
    private String url;
    public RequiredDocument() {
    }
}
