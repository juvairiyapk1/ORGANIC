package com.timeco.application.model.order;

import com.timeco.application.model.product.Product;
import com.timeco.application.model.user.User;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class WishList {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long wishListId;

    @OneToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToMany
    @JoinTable(name = "WishListProduct",
               joinColumns = @JoinColumn(name = "wishListId"),
               inverseJoinColumns = @JoinColumn(name = "productId"))
    private Set<Product> products = new HashSet<>();

    public WishList() {
    }

    public Long getWishListId() {
        return wishListId;
    }

    public void setWishListId(Long wishListId) {
        this.wishListId = wishListId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }
}
