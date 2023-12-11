package com.timeco.application.Service.categoryservice;

import com.timeco.application.Dto.CategoryOfferDto;
import com.timeco.application.model.category.CategoryOffer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryOfferService {
    void addCategoryOffer(CategoryOfferDto categoryOfferDto, Long userId);

    void updateProductOffer(CategoryOfferDto categoryOfferDto, Long categoryOfferId);

    void activeOffer(Long categoryOfferId);

    void nonActiveOffer(Long categoryOfferId);

    void addCategoryOfferToCategory(Long categoryOfferId, Long[] categoryIds);

//    List<CategoryOffer> getAllByIsEnabled();
}
