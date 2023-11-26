package com.timeco.application.Service.payment;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.timeco.application.Repository.WalletRepository;
import com.timeco.application.Repository.WalletTransactionRepository;
import com.timeco.application.model.order.PurchaseOrder;
import com.timeco.application.model.order.Wallet;
import com.timeco.application.model.order.WalletTransaction;
import com.timeco.application.model.user.Address;
import com.timeco.application.model.user.User;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private WalletTransactionRepository walletTransactionRepository;

//    @Override
//    public void handleRazorpayPayment(Map<String, Object> response, double totalAmount, PurchaseOrder purchaseOrder, String username, Address address) throws RazorpayException {
//        System.out.println("55555555555555555555"+username);
//
//        RazorpayClient razorpay = new RazorpayClient("rzp_test_bgyWOWhe1pDEcz", "4mkCzKfIfbIXXJsHBnyuVTDI");
//
//        JSONObject orderRequest = new JSONObject();
//        orderRequest.put("amount", (long) (totalAmount * 100)); // Amount should be in paise
//        orderRequest.put("currency", "INR");
//        orderRequest.put("receipt", "receipt#1");
//
//        JSONObject notes = new JSONObject();
//        notes.put("notes_key_1", "Tea, Earl Grey, Hot");
//        orderRequest.put("notes", notes);
//
//        Order order = razorpay.orders.create(orderRequest);
//
//        response.put("isValid", false);
//        response.put("orderId", order.get("id"));
//
//        response.put("amount", order.get("amount"));
//        response.put("purchaseId", purchaseOrder.getOrderId());
//        response.put("email", username);
//        response.put("username", address != null ? address.getFirstName() : "");
//        response.put("contact", address != null ? address.getPhoneNumber() : "");
//
//    }
//
//    @Override
//    public void handleWalletPayment(Map<String, Object> response, double totalAmount, User user, PurchaseOrder purchaseOrder) {
//
//        Wallet userWallet = walletRepository.findByUser(user);
//
//        if (userWallet != null) {
//            double walletBalance = userWallet.getWalletAmount();
//
//            if (walletBalance >= totalAmount) {
//                // Sufficient funds in the wallet
//                userWallet.withdraw(totalAmount);
//
//                // Record the wallet transaction
//                WalletTransaction walletTransaction = new WalletTransaction();
//                walletTransaction.setWallet(userWallet);
//                walletTransaction.setAmount(totalAmount);
//                walletTransaction.setTransactionType("DEBIT");
//                walletTransaction.setTransactionTime(LocalDateTime.now());
//                walletTransactionRepository.save(walletTransaction);
//
//                response.put("isValid", true);
//                response.put("orderId", "Wallet payment");
//                response.put("amount", totalAmount);
//                response.put("purchaseId", purchaseOrder.getOrderId());
//                response.put("email", user.getEmail());
//                response.put("username", user.getFirstName());
//                response.put("contact", user.getPhoneNumber());
//            } else {
//                // Insufficient funds in the wallet
//                response.put("isValid", false);
//                response.put("error", "Insufficient funds in the wallet");
//            }
//        } else {
//            // User doesn't have a wallet
//            response.put("isValid", false);
//            response.put("error", "User does not have a wallet");
//        }
//    }


//    private static final String RAZORPAY_KEY_ID = "rzp_test_bgyWOWhe1pDEcz";
//    private static final String RAZORPAY_KEY_SECRET = "4mkCzKfIfbIXXJsHBnyuVTDI";




}