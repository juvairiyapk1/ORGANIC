package com.timeco.application.web.usercontrollers;

import com.timeco.application.Repository.UserRepository;
import com.timeco.application.Repository.WalletRepository;
import com.timeco.application.Repository.WalletTransactionRepository;
import com.timeco.application.model.order.Wallet;
import com.timeco.application.model.order.WalletTransaction;
import com.timeco.application.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class WalletController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletTransactionRepository walletTransactionRepository;

    @Autowired
    private WalletRepository walletRepository;

    @GetMapping("/wallet")
    public String getWalletBalance(Principal principal, Model model)
    {
        String userName=principal.getName();
        User user=userRepository.findByEmail(userName);
        System.out.println("user wallet is ----------------" + user.getWallet());
        Wallet userWallet=walletRepository.findByUser(user);
        System.out.println("555555555555555555555555555userWallet"+userWallet);

        if (userWallet != null)
        {
            double walletBalance=userWallet.getWalletAmount();
            List<WalletTransaction>transactionHistory=walletTransactionRepository.findByWallet(userWallet);

            model.addAttribute("walletBalance",walletBalance);
            model.addAttribute("transactionHistory",transactionHistory);
        }else {
            model.addAttribute("walletBalance",0.0);
        }
        return "Wallet";
    }




}
