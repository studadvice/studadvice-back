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
public class Faq {
    @Id
    @Field("_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
    @TextIndexed
    @NotNull(message = "Please, give a question")
    private String question;
    @TextIndexed
    @NotNull(message = "Please, give a response")
    private String response;
    @NotNull(message = "Please, give an image")
    private String imageId;

    public Faq() {
    }
}
