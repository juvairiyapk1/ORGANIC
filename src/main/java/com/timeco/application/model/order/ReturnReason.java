package com.timeco.application.model.order;

import javax.persistence.*;

@Entity
public class ReturnReason {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long returnReasonId;

    private String returnReason;

    private String comment;

    @OneToOne
    @JoinColumn(name = "orderId")
    private PurchaseOrder order;

    public Long getReturnReasonId() {
        return returnReasonId;
    }

    public void setReturnReasonId(Long returnReasonId) {
        this.returnReasonId = returnReasonId;
    }

    public String getReturnReason() {
        return returnReason;
    }

    public void setReturnReason(String returnReason) {
        this.returnReason = returnReason;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public PurchaseOrder getOrder() {
        return order;
    }

    public void setOrder(PurchaseOrder order) {
        this.order = order;
    }

    public ReturnReason() {
    }
}
