package com.timeco.application.Repository;

import com.timeco.application.model.order.Wallet;
import com.timeco.application.model.order.WalletTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WalletTransactionRepository extends JpaRepository<WalletTransaction,Long> {
    List<WalletTransaction> findByWallet(Wallet userWallet);

}
