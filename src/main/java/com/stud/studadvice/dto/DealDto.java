package com.stud.studadvice.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

@Getter
@Setter
public class DealDto {
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
    private Integer rating = 0;
    @NotNull(message = "Please, give an url for your deal")
    private String url;

    public DealDto() {
    }
}
