package com.stud.studadvice.service;

import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import com.stud.studadvice.dto.FaqDto;
import com.stud.studadvice.entity.Faq;
import com.stud.studadvice.exception.FaqException;
import com.stud.studadvice.exception.ImageException;
import com.stud.studadvice.repository.faq.FaqRepository;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
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
import java.util.stream.Collectors;

@Service
public class FaqService {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private FaqRepository faqRepository;

    @Autowired
    private ModelMapper modelMapper;

    public Page<FaqDto> getAllQuestionAndResponseCombinations(Pageable pageable) {
        return faqRepository.findAll(pageable).map(faq -> modelMapper.map(faq, FaqDto.class));
    }

    public FaqDto getQuestionAndResponseCombinationById(ObjectId id) throws FaqException {
        return faqRepository.findById(id)
                .map(faq -> modelMapper.map(faq, FaqDto.class))
                .orElseThrow(() -> new FaqException("Question and response combination not found"));
    }

    public FaqDto createQuestionAndResponseCombination(Faq newFaq, MultipartFile imageFile) throws ImageException {
        return saveFaq(newFaq, imageFile);
    }

    public FaqDto updateQuestionAndResponseCombination(ObjectId id, Faq updatedFaq, MultipartFile imageFile) throws FaqException, ImageException {
        return faqRepository.findById(id)
                .map(existingFaq -> {
                    existingFaq.setQuestion(updatedFaq.getQuestion());
                    existingFaq.setResponse(updatedFaq.getResponse());
                    try {
                        return saveFaq(existingFaq, imageFile);
                    } catch (ImageException e) {
                        throw new RuntimeException(e);
                    }
                })
                .orElseThrow(() -> new FaqException("Question and response combination not found"));
    }



    public void deleteQuestionAndResponseCombination(ObjectId id) throws FaqException {
        if (faqRepository.existsById(id)) {
            faqRepository.deleteById(id);
        } else {
            throw new FaqException("Question and response combination not found");
        }
    }


    public Page<FaqDto> searchQuestionAndResponseCombinations(String searchText, Pageable pageable) {
        if (searchText != null && !searchText.isEmpty()) {
            TextCriteria criteria = TextCriteria.forDefaultLanguage().matching(searchText);

            Query query = TextQuery.queryText(criteria).sortByScore();

            query.with(pageable);

            long total = mongoTemplate.count(query, Faq.class);

            List<Faq> faqList = mongoTemplate.find(query, Faq.class);
            List<FaqDto> faqDtos = faqList.stream()
                    .map(faq -> modelMapper.map(faq, FaqDto.class))
                    .collect(Collectors.toList());

            return PageableExecutionUtils.getPage(faqDtos, pageable, () -> total);
        } else {
            return getAllQuestionAndResponseCombinations(pageable);
        }
    }

    private FaqDto saveFaq(Faq faq, MultipartFile imageFile) throws ImageException {
        try {
            String savedImageId = storeImage(imageFile);
            faq.setImageId(savedImageId);

            Faq savedFaq = faqRepository.save(faq);

            return modelMapper.map(savedFaq, FaqDto.class);
        } catch (IOException e) {
            throw new ImageException(e.getMessage());
        }
    }

    private String storeImage(MultipartFile imageFile) throws IOException {
        GridFSUploadOptions options = new GridFSUploadOptions()
                .metadata(new Document("contentType", imageFile.getContentType())
                        .append("contentSize", imageFile.getSize()));
        return gridFsTemplate.store(imageFile.getInputStream(), Objects.requireNonNull(imageFile.getOriginalFilename()), options).toString();
    }
}
