package com.timeco.application.model.order;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class PaymentMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentMethodId;

    private boolean isEnable;
    @Column(unique = true,nullable = true)
    private String paymentMethodName;

    @OneToMany(mappedBy="paymentMethod")
    private List<PurchaseOrder> orders = new ArrayList<>();

    public PaymentMethod() {
    }

    public Long getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(Long paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }

    public String getPaymentMethodName() {
        return paymentMethodName;
    }

    public void setPaymentMethodName(String paymentMethodName) {
        this.paymentMethodName = paymentMethodName;
    }

    public List<PurchaseOrder> getOrders() {
        return orders;
    }

    public void setOrders(List<PurchaseOrder> orders) {
        this.orders = orders;
    }

    public PaymentMethod(Long paymentMethodId, boolean isEnable, String paymentMethodName, List<PurchaseOrder> orders) {
        this.paymentMethodId = paymentMethodId;
        this.isEnable = isEnable;
        this.paymentMethodName = paymentMethodName;
        this.orders = orders;
    }
}
