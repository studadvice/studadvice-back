package com.stud.studadvice.service;

import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import com.stud.studadvice.exception.DealException;
import com.stud.studadvice.exception.ImageException;
import com.stud.studadvice.entity.Deal;
import com.stud.studadvice.repository.deals.DealsRepository;

import org.bson.Document;
import org.bson.types.ObjectId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import java.util.Optional;

@Service
public class DealsService {
    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private DealsRepository dealsRepository;

    public Page<Deal> getDeals(Pageable pageable) {
        return dealsRepository.findAll(pageable);
    }

    public Deal createDeal(Deal deal,MultipartFile imageFile) throws ImageException {
        try{
            String imageId = storeImage(imageFile);
            deal.setImageId(imageId);
            return dealsRepository.save(deal);
        }
        catch (IOException ioException){
            throw new ImageException("Error when storing the image");
        }
    }

    public Deal updateDeal(ObjectId dealId, Deal updatedDeal,MultipartFile imageFile) throws DealException, ImageException {
        Optional<Deal> existingDeal = dealsRepository.findById(dealId);
        if (existingDeal.isPresent()) {
            Deal dealToUpdate = existingDeal.get();

            dealToUpdate.setTitle(updatedDeal.getTitle());
            dealToUpdate.setDescription(updatedDeal.getDescription());
            dealToUpdate.setCategory(updatedDeal.getCategory());
            dealToUpdate.setStartDate(updatedDeal.getStartDate());
            dealToUpdate.setEndDate(updatedDeal.getEndDate());

            try{
                String imageId = storeImage(imageFile);
                dealToUpdate.setImageId(imageId);
                return dealsRepository.save(dealToUpdate);
            }
            catch (IOException ioException){
                throw new ImageException("Error when storing the image");
            }
        } else {
            throw new DealException("Student deal not found");
        }
    }

    public void deleteDeal(ObjectId dealId) throws DealException {
        if (dealsRepository.existsById(dealId)) {
            dealsRepository.deleteById(dealId);
        } else {
            throw new DealException("Student deal not found");
        }
    }

    public Deal getDealById(ObjectId dealId) throws DealException {
        return dealsRepository.findById(dealId)
                .orElseThrow(() -> new DealException("Student deal not found"));
    }

    public Page<Deal> searchDeals(String searchText, Pageable pageable) {
        TextCriteria criteria = TextCriteria.forDefaultLanguage()
                .matching(searchText);

        Query query = TextQuery.queryText(criteria)
                .sortByScore();

        query.with(pageable);

        long total = mongoTemplate.count(query, Deal.class);

        List<Deal> processes = mongoTemplate.find(query, Deal.class);

        return PageableExecutionUtils.getPage(processes, pageable, () -> total);
    }

    public String storeImage(MultipartFile imageFile) throws IOException {
        GridFSUploadOptions options = new GridFSUploadOptions()
                .metadata(new Document("contentType", imageFile.getContentType())
                        .append("contentSize", imageFile.getSize()));
        return gridFsTemplate.store(imageFile.getInputStream(), Objects.requireNonNull(imageFile.getOriginalFilename()),options).toString();
    }
}
