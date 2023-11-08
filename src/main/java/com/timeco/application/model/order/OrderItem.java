package com.timeco.application.model.order;

import com.timeco.application.model.cart.Cart;
import com.timeco.application.model.product.Product;

import javax.persistence.*;

@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartItemId;

    private Integer orderItemCount;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productId")
    private Product product;
    @ManyToOne
    @JoinColumn(name = "orderId")
    private PurchaseOrder order;


    public OrderItem(Long cartItemId, Integer orderItemCount, Product product, PurchaseOrder order) {
        this.cartItemId = cartItemId;
        this.orderItemCount = orderItemCount;
        this.product = product;
        this.order = order;
    }

    public Long getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(Long cartItemId) {
        this.cartItemId = cartItemId;
    }

    public Integer getOrderItemCount() {
        return orderItemCount;
    }

    public void setOrderItemCount(Integer orderItemCount) {
        this.orderItemCount = orderItemCount;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public PurchaseOrder getOrder() {
        return order;
    }

    public void setOrder(PurchaseOrder order) {
        this.order = order;
    }
}
