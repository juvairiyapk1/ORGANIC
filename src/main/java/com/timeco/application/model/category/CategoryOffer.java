package com.timeco.application.model.category;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
public class CategoryOffer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryOfferId;

    @Column(unique=true)
    private String categoryOfferName;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate ExpiryDate;

    private Double discountCategoryOffer;


//    private boolean isEnabled=true;
    private boolean isActive=false;

    @OneToMany(mappedBy = "categoryOffer",cascade={CascadeType.MERGE,CascadeType.MERGE},fetch = FetchType.EAGER)
    private Set<Category>categories=new HashSet<>();

    public Long getCategoryOfferId() {
        return categoryOfferId;
    }

    public void setCategoryOfferId(Long categoryOfferId) {
        this.categoryOfferId = categoryOfferId;
    }

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

//    public boolean isEnabled() {
//        return isEnabled;
//    }
//
//    public void setEnabled(boolean enabled) {
//        isEnabled = enabled;
//    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public CategoryOffer() {
    }
}
