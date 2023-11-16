package com.timeco.application.Dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class CouponDto {

    private Integer couponId;

    private String couponCode;

    private String description;


    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate expiryDate;

    private Double percentage;

    private Double minimumPurchaseAmount;

    private Boolean isActive;

    private Integer cartItemsCount;

    private Integer usageCount=0;

    public CouponDto(String couponCode, String description, LocalDate expiryDate, Double percentage, Double minimumPurchaseAmount, Boolean isActive, Integer cartItemsCount, Integer usageCount) {
        this.couponCode = couponCode;
        this.description = description;
        this.expiryDate = expiryDate;
        this.percentage = percentage;
        this.minimumPurchaseAmount = minimumPurchaseAmount;
        this.isActive = isActive;
        this.cartItemsCount = cartItemsCount;
        this.usageCount = usageCount;
    }

    public Integer getCouponId() {
        return couponId;
    }

    public void setCouponId(Integer couponId) {
        this.couponId = couponId;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public Double getMinimumPurchaseAmount() {
        return minimumPurchaseAmount;
    }

    public void setMinimumPurchaseAmount(Double minimumPurchaseAmount) {
        this.minimumPurchaseAmount = minimumPurchaseAmount;
    }

    public Boolean isActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Integer getCartItemsCount() {
        return cartItemsCount;
    }

    public void setCartItemsCount(Integer cartItemsCount) {
        this.cartItemsCount = cartItemsCount;
    }

    public Integer getUsageCount() {
        return usageCount;
    }

    public void setUsageCount(Integer usageCount) {
        this.usageCount = usageCount;
    }
}
