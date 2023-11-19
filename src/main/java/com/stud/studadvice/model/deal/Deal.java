package com.stud.studadvice.model.deal;

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
    private ObjectId id;
    @TextIndexed
    private String title;
    @TextIndexed
    private String description;
    private String category;
    private String image;
    private String startDate;
    private String endDate;

    public Deal() {
    }
}
