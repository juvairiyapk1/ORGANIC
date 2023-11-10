package com.timeco.application.model.order;

import com.timeco.application.model.cart.Cart;
import com.timeco.application.model.product.Product;

import javax.persistence.*;

@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderItemId;

    private Integer orderItemCount;

    private String orderStatus;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productId")
    private Product product;
    @ManyToOne
    @JoinColumn(name = "orderId")
    private PurchaseOrder order;


    public OrderItem(Long orderItemId, Integer orderItemCount, Product product,String orderStatus, PurchaseOrder order) {
        this.orderItemId = orderItemId;
        this.orderItemCount = orderItemCount;
        this.product = product;
        this.orderStatus=orderStatus;
        this.order = order;
    }

    public OrderItem() {

    }

    public Long getCartItemId() {
        return orderItemId;
    }

    public void setCartItemId(Long cartItemId) {
        this.orderItemId = cartItemId;
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

    public Long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
