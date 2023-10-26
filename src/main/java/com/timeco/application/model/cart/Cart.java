package com.timeco.application.model.cart;

import com.timeco.application.model.user.User;

import javax.persistence.*;
import java.util.List;

@Entity
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long cartId;
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private List<CartItem> cartItems;


    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private double totalPrice;

    public Cart(Long cartId, User user, List<CartItem> cartItems) {
        this.cartId = cartId;
        this.user = user;
        this.cartItems = cartItems;
    }

    public Cart() {

    }

    public Cart(User user) {
        this.user=user;
    }

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}