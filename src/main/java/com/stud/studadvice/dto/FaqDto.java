package com.stud.studadvice.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
@Getter
@Setter
public class FaqDto {
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
    @NotNull(message = "Please, give a question")
    private String question;
    @NotNull(message = "Please, give a response")
    private String response;
    private String imageId;
    public FaqDto() {
    }
}
