package com.stud.studadvice.model.administrative;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;

@Getter
@Setter
public class Step {
    private int stepNumber;
    private String name;
    private String description;
    private String image;
    @DocumentReference
    private List<RequiredDocument> requiredDocuments;

    public Step() {
    }
}
