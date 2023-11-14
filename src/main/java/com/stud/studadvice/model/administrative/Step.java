package com.stud.studadvice.model.administrative;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;

@Getter
@Setter
public class Step {
    private int stepNumber;
    private Informations informations;
    @DocumentReference
    private List<RequiredDocument> requiredDocumentList;

    public Step() {
    }
}
