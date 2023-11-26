package com.timeco.application.model.product;


import com.timeco.application.model.cart.CartItem;
import com.timeco.application.model.category.Category;
import com.timeco.application.model.category.Subcategory;
import com.timeco.application.model.order.WishList;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="product_name",nullable = false)
    private String productName;

    @Transient
    @Column(name="current_state",nullable = false)
    private String current_state;

    @Column(name="description",nullable = false)
    private String description;

    @Column(name="quantity",nullable = false)
    private Integer quantity;

    @Column(name="price",nullable = false)
    private Double price;

    private String productImages;

    @ManyToMany(mappedBy = "products")
    private Set<WishList> wishList = new HashSet<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Set<CartItem> cartItems = new HashSet<>();

    @ManyToOne(fetch =FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "category_id",referencedColumnName = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "subcategory_id", referencedColumnName = "subcategory_Id")
    private Subcategory subcategory; // The subcategory to which this product belongs


//    public List<ProductImage> getProductImages() {
//        return productImages;
//    }
//
//    public void setProductImages(List<ProductImage> productImages) {
//        this.productImages = productImages;
//    }


    public Product(String productName, String current_state, String description, Integer quantity, Double price, String productImages, Set<CartItem> cartItems, Category category, Subcategory subcategory) {
        this.productName = productName;
        this.current_state = current_state;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
        this.productImages = productImages;
        this.cartItems = cartItems;
        this.category = category;
        this.subcategory = subcategory;
    }

    public Product(Long id, String productName, String current_state, String description, Integer quantity, Double price, String productImages, Category category) {
        this.id = id;
        this.productName = productName;
        this.current_state = current_state;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
        this.productImages=productImages;
        this.category = category;
    }

    public Product() {

    }

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
        // Set the current_state based on the quantity
        if (quantity > 0) {
            return "<span class='stock-in'>Stock In</span>";
        } else {
            return "<span class='stock-out'>Stock Out</span>";
        }

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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Subcategory getSubcategory() {
        return subcategory;
    }

    public String getProductImages() {
        return productImages;
    }

    public void setProductImages(String productImages) {
        this.productImages = productImages;
    }

    public void setSubcategory(Subcategory subcategory) {
        this.subcategory = subcategory;
    }

    public Set<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(Set<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public Set<WishList> getWishList() {
        return wishList;
    }

    public void setWishList(Set<WishList> wishList) {
        this.wishList = wishList;
    }


}
