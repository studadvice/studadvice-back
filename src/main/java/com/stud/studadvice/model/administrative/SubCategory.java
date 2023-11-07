package com.stud.studadvice.model.administrative;

import java.util.List;

public class SubCategory {
    private Informations informations;
    private List<AdministrativeProcess> administrativeProcessList;

    public SubCategory() {
    }

    public Informations getInformations() {
        return informations;
    }

    public void setInformations(Informations informations) {
        this.informations = informations;
    }

    public List<AdministrativeProcess> getAdministrativeProcessList() {
        return administrativeProcessList;
    }

    public void setAdministrativeProcessList(List<AdministrativeProcess> administrativeProcessList) {
        this.administrativeProcessList = administrativeProcessList;
    }
}
