package com.stud.studadvice.model.administrative;

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
public class RequiredDocument {
    @Field("_id")
    @Id
    private ObjectId id;
    @TextIndexed
    private String name;
    @TextIndexed
    private String description;
    private String image;
    public RequiredDocument() {
    }
}
