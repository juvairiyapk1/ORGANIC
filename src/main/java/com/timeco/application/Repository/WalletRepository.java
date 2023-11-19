package com.timeco.application.Repository;

import com.timeco.application.model.order.Wallet;
import com.timeco.application.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet,Long> {
    Wallet findByUser(User user);
}
