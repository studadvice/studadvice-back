package com.stud.studadvice.model.administrative;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Informations {
    private String name;
    private String description;
    private String image;
    private Integer age;
    private List<String> nationalities;
    private List<String> universities;
    public Informations() {
    }
}
