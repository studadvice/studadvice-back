package com.stud.studadvice.service;

import com.stud.studadvice.exception.AdministrativeProcessException;
import com.stud.studadvice.exception.CategoryException;
import com.stud.studadvice.model.administrative.AdministrativeProcess;
import com.stud.studadvice.model.administrative.Category;
import com.stud.studadvice.model.administrative.SubCategory;
import com.stud.studadvice.repository.administrative.AdministrativeProcessRepository;

import com.stud.studadvice.repository.categories.CategoryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdministrativeProcessService {
    @Autowired
    private AdministrativeProcessRepository administrativeProcessRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    public List<AdministrativeProcess> getAdministrativeProcess() {
        return administrativeProcessRepository.findAll();
    }

    public List<AdministrativeProcess> getAdministrativeProcess(String category, String subCategory) {
        if (category != null && subCategory != null){
            return administrativeProcessRepository.findByCategoryNameAndSubCategoryName(category, subCategory);
        }

        else if (category != null){
            return administrativeProcessRepository.findByCategoryName(category);
        }

        else if (subCategory != null){
            return administrativeProcessRepository.findBySubCategoryName(subCategory);
        }
        else{
            return administrativeProcessRepository.findAll();
        }
    }

    public AdministrativeProcess getAdministrativeProcessById(ObjectId id) throws AdministrativeProcessException {
        AdministrativeProcess process = administrativeProcessRepository.findById(id).orElse(null);
        if (process == null) {
            throw new AdministrativeProcessException("Administrative process not found");
        }
        return process;
    }

    public AdministrativeProcess createAdministrativeProcess(AdministrativeProcess administrativeProcess) throws CategoryException{
        Category existingCategory = categoryRepository.findByName(administrativeProcess.getCategoryName());
        if (existingCategory == null){
            throw new CategoryException("Category not found. Please use an existant category or create a new one");
        }

        boolean found = false;
        for(SubCategory subCategory: existingCategory.getSubCategoryList()){
            found = subCategory.getName().equals(administrativeProcess.getSubCategoryName());
        }

        if (!found){
            throw new CategoryException("Sub category not found for this category. Please use an existent sub category or create a new one");
        }

        return administrativeProcessRepository.save(administrativeProcess);
    }

    public AdministrativeProcess updateAdministrativeProcess(ObjectId id, AdministrativeProcess updatedProcess) throws AdministrativeProcessException,CategoryException {
        AdministrativeProcess existingProcess = administrativeProcessRepository.findById(id).orElse(null);
        if (existingProcess == null) {
            throw new AdministrativeProcessException("Administrative process to update not found");
        }

        Category existingCategory = categoryRepository.findByName(updatedProcess.getCategoryName());
        if (existingCategory == null){
            throw new CategoryException("Category not found. Please use an existant category or create a new one");
        }

        boolean found = false;
        for(SubCategory subCategory: existingCategory.getSubCategoryList()){
            found = subCategory.getName().equals(updatedProcess.getSubCategoryName());
        }

        if (!found){
            throw new CategoryException("Sub category not found for this category. Please use an existent sub category or create a new one");
        }

        existingProcess.setInformations(updatedProcess.getInformations());
        existingProcess.setResourceList(updatedProcess.getResourceList());
        existingProcess.setStepList(updatedProcess.getStepList());
        existingProcess.setRequiredDocumentsList(updatedProcess.getRequiredDocumentsList());
        existingProcess.setCategoryName(updatedProcess.getCategoryName());

        return administrativeProcessRepository.save(existingProcess);
    }

    public void deleteAdministrativeProcess(ObjectId id) throws AdministrativeProcessException {
        if (!administrativeProcessRepository.existsById(id)) {
            throw new AdministrativeProcessException("Administrative process to delete not found");
        }
        administrativeProcessRepository.deleteById(id);
    }
}
