package com.stud.studadvice.dto.deal;

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
public class Deal {
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
    @NotNull(message = "Please, give a title to your deal")
    private String title;
    @NotNull(message = "Please, give a description to your deal")
    private String description;
    private String category;
    private String imageId;
    private String startDate;
    private String endDate;

    public Deal() {
    }
}
