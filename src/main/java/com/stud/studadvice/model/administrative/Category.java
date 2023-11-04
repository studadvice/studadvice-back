package com.stud.studadvice.model.administrative;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
public class Category {
    private String name;
    private String image;
    private List<SubCategory> subCategoriesList;

    public Category() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<SubCategory> getSubCategoriesList() {
        return subCategoriesList;
    }

    public void setSubCategoriesList(List<SubCategory> subCategoriesList) {
        this.subCategoriesList = subCategoriesList;
    }
}
