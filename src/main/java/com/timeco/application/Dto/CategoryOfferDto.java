package com.timeco.application.Dto;

import com.timeco.application.model.category.Category;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class CategoryOfferDto {
    private Long categoryOfferId;

    @Column(unique=true)
    private String categoryOfferName;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate ExpiryDate;

    private Double discountCategoryOffer;

    private boolean isActive=false;


    public String getCategoryOfferName() {
        return categoryOfferName;
    }

    public void setCategoryOfferName(String categoryOfferName) {
        this.categoryOfferName = categoryOfferName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getExpiryDate() {
        return ExpiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        ExpiryDate = expiryDate;
    }

    public Double getDiscountCategoryOffer() {
        return discountCategoryOffer;
    }

    public void setDiscountCategoryOffer(Double discountCategoryOffer) {
        this.discountCategoryOffer = discountCategoryOffer;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
