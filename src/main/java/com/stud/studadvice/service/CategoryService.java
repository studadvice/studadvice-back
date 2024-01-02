package com.stud.studadvice.service;

import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import com.stud.studadvice.dto.AdministrativeProcessDto;
import com.stud.studadvice.dto.CategoryDto;
import com.stud.studadvice.exception.AdministrativeProcessException;
import com.stud.studadvice.exception.CategoryException;
import com.stud.studadvice.exception.ImageException;
import com.stud.studadvice.entity.AdministrativeProcess;
import com.stud.studadvice.repository.administrative.AdministrativeProcessRepository;
import com.stud.studadvice.repository.categories.CategoryRepository;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.stud.studadvice.entity.Category;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private GridFsTemplate gridFsTemplate;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private AdministrativeProcessRepository administrativeProcessRepository;

    public Page<CategoryDto> getCategories(Pageable pageable) {
        Page<Category> categoryPage = categoryRepository.findAll(pageable);

        List<CategoryDto> categoryDtos = categoryPage.getContent().stream()
                .map(category -> modelMapper.map(category, CategoryDto.class))
                .collect(Collectors.toList());

        return new PageImpl<>(categoryDtos, pageable, categoryPage.getTotalElements());
    }

    public CategoryDto getCategoryById(ObjectId categoryId) throws CategoryException {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryException("Category not found"));

        return modelMapper.map(category, CategoryDto.class);
    }

    public CategoryDto createCategory(Category category, MultipartFile imageFile)
            throws AdministrativeProcessException, ImageException {

        validateAdministrativeProcesses(category);

        try {
            String imageId = storeImage(imageFile);
            category.setImageId(imageId);
            Category createdCategory = categoryRepository.save(category);

            for (AdministrativeProcess administrativeProcess : category.getAdministrativeProcesses()) {
                AdministrativeProcess process = administrativeProcessRepository.findById(administrativeProcess.getId()).get();
                process.setCategory(String.valueOf(category.getId()));
                administrativeProcessRepository.save(process);
            }

            return modelMapper.map(createdCategory, CategoryDto.class);
        } catch (IOException ioException) {
            throw new ImageException("Error when storing the image");
        }
    }

    public void validateAdministrativeProcesses(Category category) throws AdministrativeProcessException {
        if (category.getAdministrativeProcesses() != null) {
            for (AdministrativeProcess administrativeProcess : category.getAdministrativeProcesses()) {
                if (administrativeProcessRepository.findById(administrativeProcess.getId()).isEmpty()) {
                    throw new AdministrativeProcessException("Administrative process not found");
                }
            }
        }
    }


    public CategoryDto updateCategoryById(ObjectId categoryId, Category categoryUpdated, MultipartFile imageFile)
            throws CategoryException, AdministrativeProcessException, ImageException {

        Category existingCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryException("Category not found"));

        validateAdministrativeProcesses(categoryUpdated);

        updateCategoryFields(existingCategory, categoryUpdated);

        try {
            String imageId = storeImage(imageFile);
            existingCategory.setImageId(imageId);

            Category updatedCategory = categoryRepository.save(existingCategory);

            for (AdministrativeProcess administrativeProcess : updatedCategory.getAdministrativeProcesses()) {
                AdministrativeProcess process = administrativeProcessRepository.findById(administrativeProcess.getId()).get();
                process.setCategory(String.valueOf(updatedCategory.getId()));
                administrativeProcessRepository.save(process);
            }

            return modelMapper.map(updatedCategory, CategoryDto.class);
        } catch (IOException ioException) {
            throw new ImageException("Error when storing the image");
        }
    }

    private void updateCategoryFields(Category existingCategory, Category updatedCategory) {
        existingCategory.setDescription(updatedCategory.getDescription());
        existingCategory.setName(updatedCategory.getName());
        existingCategory.setAdministrativeProcesses(updatedCategory.getAdministrativeProcesses());
    }


    public void deleteCategoryById(ObjectId categoryId) throws CategoryException {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryException("Category not found"));

        categoryRepository.delete(category);
    }

    public Page<AdministrativeProcessDto> getAdministrativeProcessByCategoryId(ObjectId categoryId, Integer age, String nationality, String university, String education, Pageable pageable) throws CategoryException {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryException("Category not found"));

        List<AdministrativeProcess> administrativeProcesses = category.getAdministrativeProcesses();

        List<AdministrativeProcess> filteredAdministrativeProcesses = administrativeProcesses.stream()
                .filter(process -> (age == null || (process.getMinAge() <= age && process.getMaxAge() >= age))
                        && (nationality == null || new HashSet<>(process.getNationalities()).contains(nationality))
                        && (university == null || new HashSet<>(process.getUniversities()).contains(university))
                        && (education == null || new HashSet<>(process.getEducations()).contains(education)))
                .toList();

        List<AdministrativeProcessDto> dtos = filteredAdministrativeProcesses.stream()
                .map(process -> modelMapper.map(process, AdministrativeProcessDto.class))
                .collect(Collectors.toList());

        if (pageable.isPaged()) {
            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), dtos.size());
            List<AdministrativeProcessDto> paginatedDtos = dtos.subList(start, end);

            long totalCount = dtos.size();

            return new PageImpl<>(paginatedDtos, pageable, totalCount);
        } else {
            return new PageImpl<>(dtos, pageable, dtos.size());
        }
    }


    public CategoryDto addAdministrativeProcessToAnExistingCategory(ObjectId categoryId, ObjectId administrativeProcessId) throws CategoryException, AdministrativeProcessException {
        Category existingCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryException("Category not found"));

        AdministrativeProcess existingAdministrativeProcess = administrativeProcessRepository.findById(administrativeProcessId)
                .orElseThrow(() -> new AdministrativeProcessException("Administrative process not found"));

        List<AdministrativeProcess> administrativeProcesses = existingCategory.getAdministrativeProcesses();

        if (administrativeProcesses != null) {
            administrativeProcesses.add(existingAdministrativeProcess);
        } else {
            administrativeProcesses = new ArrayList<>();
            administrativeProcesses.add(existingAdministrativeProcess);
        }

        existingCategory.setAdministrativeProcesses(administrativeProcesses);

        Category updatedCategory = categoryRepository.save(existingCategory);

        return modelMapper.map(updatedCategory, CategoryDto.class);
    }

    public String storeImage(MultipartFile imageFile) throws IOException {
        GridFSUploadOptions options = new GridFSUploadOptions()
                .metadata(new Document("contentType", imageFile.getContentType())
                        .append("contentSize", imageFile.getSize()));
        return gridFsTemplate.store(imageFile.getInputStream(), Objects.requireNonNull(imageFile.getOriginalFilename()),options).toString();
    }

    public Page<CategoryDto> searchAdministrativeProcess(String searchText, Pageable pageable) {
        if (searchText != null && !searchText.isEmpty()) {
            TextCriteria criteria = TextCriteria.forDefaultLanguage().matching(searchText);

            Query query = TextQuery.queryText(criteria).sortByScore();
            query.with(pageable);

            long total = mongoTemplate.count(query, AdministrativeProcess.class);

            List<AdministrativeProcess> processes = mongoTemplate.find(query, AdministrativeProcess.class);

            List<CategoryDto> dtos = new ArrayList<>();

            for (AdministrativeProcess administrativeProcess : processes) {
                Query categoryQuery = new Query(Criteria.where("administrativeProcesses").is(administrativeProcess.getId()));
                List<Category> categories = mongoTemplate.find(categoryQuery, Category.class);

                categories.forEach(cat -> {
                    CategoryDto dto = modelMapper.map(cat, CategoryDto.class);
                    dtos.add(dto);
                });
            }

            return PageableExecutionUtils.getPage(dtos, pageable, () -> total);
        } else {
            return getCategories(pageable);
        }
    }

}
