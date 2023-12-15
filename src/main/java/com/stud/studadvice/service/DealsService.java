package com.stud.studadvice.service;

import com.mongodb.client.gridfs.model.GridFSUploadOptions;

import com.stud.studadvice.dto.DealDto;
import com.stud.studadvice.exception.DealException;
import com.stud.studadvice.exception.ImageException;
import com.stud.studadvice.entity.Deal;
import com.stud.studadvice.repository.deals.DealsRepository;

import org.bson.Document;
import org.bson.types.ObjectId;

import org.modelmapper.ModelMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class DealsService {
    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private DealsRepository dealsRepository;

    @Autowired
    private ModelMapper modelMapper;

    public Page<DealDto> getDeals(Pageable pageable) {
        Page<Deal> dealPage = dealsRepository.findAll(pageable);
        List<DealDto> dealDtos = dealPage.getContent().stream()
                .map(deal -> modelMapper.map(deal, DealDto.class))
                .collect(Collectors.toList());

        return new PageImpl<>(dealDtos, pageable, dealPage.getTotalElements());
    }

    public DealDto createDeal(Deal deal, MultipartFile imageFile) throws ImageException {
        try {
            String imageId = storeImage(imageFile);
            deal.setImageId(imageId);
            Deal createdDeal = dealsRepository.save(deal);

            return modelMapper.map(createdDeal, DealDto.class);
        } catch (IOException ioException) {
            throw new ImageException("Error when storing the image");
        }
    }

    public DealDto updateDeal(ObjectId dealId, Deal updatedDeal, MultipartFile imageFile)
            throws DealException, ImageException {

        Deal dealToUpdate = dealsRepository.findById(dealId)
                .orElseThrow(() -> new DealException("Student deal not found"));

        updateDealFields(dealToUpdate, updatedDeal);

        try {
            String imageId = storeImage(imageFile);
            dealToUpdate.setImageId(imageId);

            Deal updatedDealEntity = dealsRepository.save(dealToUpdate);

            return modelMapper.map(updatedDealEntity, DealDto.class);
        } catch (IOException ioException) {
            throw new ImageException("Error when storing the image");
        }
    }

    private void updateDealFields(Deal dealToUpdate, Deal updatedDeal) {
        dealToUpdate.setTitle(updatedDeal.getTitle());
        dealToUpdate.setDescription(updatedDeal.getDescription());
        dealToUpdate.setCategory(updatedDeal.getCategory());
        dealToUpdate.setStartDate(updatedDeal.getStartDate());
        dealToUpdate.setEndDate(updatedDeal.getEndDate());
    }

    public void deleteDeal(ObjectId dealId) throws DealException {
        if (dealsRepository.existsById(dealId)) {
            dealsRepository.deleteById(dealId);
        } else {
            throw new DealException("Student deal not found");
        }
    }

    public DealDto getDealById(ObjectId dealId) throws DealException {
        Deal deal = dealsRepository.findById(dealId)
                .orElseThrow(() -> new DealException("Student deal not found"));

        return modelMapper.map(deal, DealDto.class);
    }

    public Page<DealDto> searchDeals(String searchText, Pageable pageable) {
        TextCriteria criteria = TextCriteria.forDefaultLanguage()
                .matching(searchText);

        Query query = TextQuery.queryText(criteria)
                .sortByScore();

        query.with(pageable);

        long total = mongoTemplate.count(query, Deal.class);

        List<Deal> deals = mongoTemplate.find(query, Deal.class);
        List<DealDto> dealDtos = deals.stream()
                .map(deal -> modelMapper.map(deal, DealDto.class))
                .collect(Collectors.toList());

        return PageableExecutionUtils.getPage(dealDtos, pageable, () -> total);
    }

    public String storeImage(MultipartFile imageFile) throws IOException {
        GridFSUploadOptions options = new GridFSUploadOptions()
                .metadata(new Document("contentType", imageFile.getContentType())
                        .append("contentSize", imageFile.getSize()));
        return gridFsTemplate.store(imageFile.getInputStream(), Objects.requireNonNull(imageFile.getOriginalFilename()), options).toString();
    }
}
