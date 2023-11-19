package com.timeco.application.model.order;

import com.timeco.application.model.user.User;

import javax.persistence.*;

@Entity
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long walletId;

    private Double walletAmount;

    @OneToOne
    private User user;

    public Wallet(Long walletId, Double walletAmount, User user) {
        this.walletId = walletId;
        this.walletAmount = walletAmount;
        this.user = user;
    }

    public Long getWalletId() {
        return walletId;
    }

    public void setWalletId(Long walletId) {
        this.walletId = walletId;
    }

    public Double getWalletAmount() {
        return walletAmount;
    }

    public void setWalletAmount(Double walletAmount) {
        this.walletAmount = walletAmount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
