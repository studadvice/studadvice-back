package com.stud.studadvice.service;

import com.stud.studadvice.exception.DealException;
import com.stud.studadvice.model.deal.Deal;
import com.stud.studadvice.repository.deals.DealsRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DealsService {
    @Autowired
    private DealsRepository dealsRepository;

    public List<Deal> getAllDeals() {
        return dealsRepository.findAll();
    }

    public Deal createDeal(Deal deal) {
        return dealsRepository.save(deal);
    }

    public Deal updateDeal(ObjectId dealId, Deal updatedDeal) throws DealException {
        Optional<Deal> existingDeal = dealsRepository.findById(dealId);
        if (existingDeal.isPresent()) {
            Deal dealToUpdate = existingDeal.get();

            dealToUpdate.setTitle(updatedDeal.getTitle());
            dealToUpdate.setDescription(updatedDeal.getDescription());
            dealToUpdate.setCategory(updatedDeal.getCategory());
            dealToUpdate.setImage(updatedDeal.getImage());
            dealToUpdate.setStartDate(updatedDeal.getStartDate());
            dealToUpdate.setEndDate(updatedDeal.getEndDate());

            return dealsRepository.save(dealToUpdate);
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
}
