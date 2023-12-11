package com.timeco.application.Service.wallet;

import com.timeco.application.Repository.WalletRepository;
import com.timeco.application.Repository.WalletTransactionRepository;
import com.timeco.application.model.order.Wallet;
import com.timeco.application.model.order.WalletTransaction;
import com.timeco.application.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class WalletServiceImpl implements WalletService{

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private WalletTransactionRepository walletTransactionRepository;
    @Override
    public void addAmountToWallet(User verifyCustomer, double referralBonusAmount) {

        Wallet wallet=verifyCustomer.getWallet();


        if (wallet != null)
        {
            wallet.setWalletAmount(referralBonusAmount);
            walletRepository.save(wallet);
            System.out.println("wallet amount of the user credited successfully "+wallet.getWalletAmount());

            WalletTransaction walletTransaction=new WalletTransaction();
            walletTransaction.setWallet(verifyCustomer.getWallet());
            walletTransaction.setAmount(referralBonusAmount);
            walletTransaction.setTransactionType("Referral amount");
            walletTransaction.setTransactionTime(LocalDateTime.now());
            walletTransactionRepository.save(walletTransaction);
        }




    }
}
