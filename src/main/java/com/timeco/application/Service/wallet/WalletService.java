package com.timeco.application.Service.wallet;

import com.timeco.application.model.user.User;
import org.springframework.stereotype.Service;

@Service
public interface WalletService {

    void addAmountToWallet(User verifyCustomer, double referralBonusAmount);
}
