package com.timeco.application.model.order;

import com.timeco.application.model.user.User;
import net.bytebuddy.implementation.bind.MethodDelegationBinder;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer couponId;

    private String couponCode;

    private String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate expiryDate;

    private Double percentage;

    private Double minimumPurchaseAmount;

    private boolean isActive;

    private Integer cartItemsCount;

    private Integer usageCount=0;

    @OneToMany(mappedBy="coupon")
    private List<PurchaseOrder> ordersList;

    @ManyToMany(mappedBy = "coupons")
    private Set<User> users = new HashSet<>();


    public Coupon(String couponCode, String description, LocalDate expiryDate, Double percentage,
                  Double minimumPurchaseAmount, boolean isActive, Integer cartItemsCount) {
        super();
        this.couponCode = couponCode;
        this.description = description;
        this.expiryDate = expiryDate;
        this.percentage = percentage;
        this.minimumPurchaseAmount = minimumPurchaseAmount;
        this.isActive = isActive;
        this.cartItemsCount = cartItemsCount;
    }

    public Coupon() {

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


    public boolean isActive() {
        return isActive ;
    }

    public void setActive(boolean active) {
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

    public List<PurchaseOrder> getOrdersList() {
        return ordersList;
    }

    public void setOrdersList(List<PurchaseOrder> ordersList) {
        this.ordersList = ordersList;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
