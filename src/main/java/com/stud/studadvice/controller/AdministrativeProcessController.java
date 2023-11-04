package com.stud.studadvice.controller;

import com.stud.studadvice.model.administrative.AdministrativeProcess;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/administrative-process")
public class AdministrativeProcessController {

    @GetMapping("/categories")
    private List<String> getAdministrativeProcessCategories(){
        return null;
    }

    @GetMapping("/categories/{category}/sub-categories")
    private List<String> getCategorySubCategories(@PathVariable String category){
        return null;
    }

    @GetMapping
    private List<AdministrativeProcess> getAdministrativeProcess(@RequestParam String category, @RequestParam String subCategory){
        return null;
    }

    @GetMapping("/{id}")
    public AdministrativeProcess getAdministrativeProcessById(@PathVariable String id) {
        return null;
    }

    @PostMapping("/")
    public AdministrativeProcess createAdministrativeProcess(@RequestBody AdministrativeProcess administrativeProcess) {
        return null;
    }

    @PutMapping("/{id}")
    public AdministrativeProcess updateAdministrativeProcess(@PathVariable String id, @RequestBody AdministrativeProcess updatedProcess) {
        return null;
    }

    @DeleteMapping("/{id}")
    public void deleteAdministrativeProcess(@PathVariable String id) {
    }

}
