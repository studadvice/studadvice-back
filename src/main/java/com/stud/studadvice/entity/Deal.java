package com.stud.studadvice.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

import org.bson.types.ObjectId;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Document
public class Deal {
    @Id
    @Field("_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
    @TextIndexed
    @NotNull(message = "Please, give a title for your deal")
    private String title;
    @TextIndexed
    @NotNull(message = "Please, give a description for your deal")
    private String description;
    private String category;
    @NotNull(message = "Please, give an image for your deal")
    private String imageId;
    private String startDate;
    private String endDate;
    private Integer rating;
    @NotNull(message = "Please, give an url for your deal")
    private String url;
    private Integer numberOfRating = 0;

    public Deal() {
    }
}
