package com.timeco.application.Dto;

import com.timeco.application.model.category.Category;
//import com.timeco.application.model.category.Subcategory;
import com.timeco.application.model.product.ProductOffer;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.Min;

public class ProductDto {

    private Long id;
    private String productName;
    private String current_state;
    private String description;
    @Min(0)
    private Integer quantity;
    @DecimalMax("0.01")
    private Double price;
    private Long categoryId;
    private Long subcategoryId;
    private String imageName;
    private  Long productOfferId;

    // Add a field to hold the Category object
    private Category category;
//    private Subcategory subcategory;

    private String productImages;

    private ProductOffer productOffer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCurrent_state() {
        return current_state;
    }

    public void setCurrent_state(String current_state) {
        this.current_state = current_state;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    // Getter and Setter for Category
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Long getSubcategoryId() {
        return subcategoryId;
    }

    public void setSubcategoryId(Long subcategoryId) {
        this.subcategoryId = subcategoryId;
    }

//    public Subcategory getSubcategory() {
//        return subcategory;
//    }
//
//    public void setSubcategory(Subcategory subcategory) {
//        this.subcategory = subcategory;
//    }

    public ProductDto() {
        super();
    }

    public ProductDto(String productName, String current_state, String description, Integer quantity, Double price, Long categoryId, String imageName) {
        this.productName = productName;
        this.current_state = current_state;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
        this.categoryId = categoryId;
        this.imageName = imageName;
    }

    public ProductDto(String productName, String current_state, String description, Integer quantity, Double price, Long categoryId, String imageName, Category category) {
        this.productName = productName;
        this.current_state = current_state;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
        this.categoryId = categoryId;
//        this.subcategoryId = subcategoryId;
        this.imageName = imageName;
        this.category = category;
//        this.subcategory = subcategory;
    }

    public String getProductImages() {
        return productImages;
    }

    public ProductOffer getProductOffer() {
        return productOffer;
    }

    public void setProductOffer(ProductOffer productOffer) {
        this.productOffer = productOffer;
    }

    public void setProductImages(String productImages) {
        this.productImages = productImages;
    }

    public Long getProductOfferId() {
        return productOfferId;
    }

    public void setProductOfferId(Long productOfferId) {
        this.productOfferId = productOfferId;
    }

    public ProductDto(Long productOfferId) {
        this.productOfferId = productOfferId;
    }
}
