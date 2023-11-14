package com.timeco.application.model.order;

import com.timeco.application.model.user.Address;
import com.timeco.application.model.user.User;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class PurchaseOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;
    @OneToOne
    private Address address;
    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL)
    private List<OrderItem>orderItems=new ArrayList<>();

    @ManyToOne
    private PaymentMethod paymentMethod;
    @ManyToOne
    private User user;

    @ManyToOne
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;


    private LocalDate orderedDate;

//    private String orderStatus;

    private Double orderAmount;

    private String transcationId;

//    private Integer orderedQuantity;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "purchase_order_order_statuses",
            joinColumns = @JoinColumn(name = "purchase_order_id"),
            inverseJoinColumns = @JoinColumn(name = "order_status_id"))
    private Set<OrderStatus> possibleOrderStatuses = new HashSet<>();

    public PurchaseOrder(Long orderId, Address address, List<OrderItem> orderItems, PaymentMethod paymentMethod, User user, LocalDate orderedDate, Double orderAmount, String transcationId, Set<OrderStatus> possibleOrderStatuses) {
        this.orderId = orderId;
        this.address = address;
        this.orderItems = orderItems;
        this.paymentMethod = paymentMethod;
        this.user = user;
        this.orderedDate = orderedDate;
//        this.orderStatus = orderStatus;
        this.orderAmount = orderAmount;
        this.transcationId = transcationId;
        this.possibleOrderStatuses = possibleOrderStatuses;
    }

    public PurchaseOrder(Coupon coupon) {
        this.coupon = coupon;
    }

    public PurchaseOrder() {
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getOrderedDate() {
        return orderedDate;
    }

    public void setOrderedDate(LocalDate orderedDate) {
        this.orderedDate = orderedDate;
    }



    public Double getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(Double orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getTranscationId() {
        return transcationId;
    }

    public void setTranscationId(String transcationId) {
        this.transcationId = transcationId;
    }


    public Set<OrderStatus> getPossibleOrderStatuses() {
        return possibleOrderStatuses;
    }

    public void setPossibleOrderStatuses(Set<OrderStatus> possibleOrderStatuses) {
        this.possibleOrderStatuses = possibleOrderStatuses;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }
}
