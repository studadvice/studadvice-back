package com.stud.studadvice.model.administrative;

public class RequiredDocument {
    private Long id;
    private Informations informations;

    public RequiredDocument() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Informations getInformations() {
        return informations;
    }

    public void setInformations(Informations informations) {
        this.informations = informations;
    }
}
