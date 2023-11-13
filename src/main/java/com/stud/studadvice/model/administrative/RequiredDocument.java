package com.stud.studadvice.model.administrative;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Getter
@Setter
@Document
public class RequiredDocument {
    @Field("_id")
    @Id
    private ObjectId id;
    private Informations informations;
    @DocumentReference(lazy = true)
    private List<Step> stepList;
    public RequiredDocument() {
    }
}
