package com.stud.studadvice.model.administrative;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

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
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
    private String name;
    private String description;
    private String image;
    @DocumentReference
    private List<AdministrativeProcess> administrativeProcesses;
    public Category() {
    }

}
