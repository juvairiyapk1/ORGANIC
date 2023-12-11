package com.timeco.application.Service.payment;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.timeco.application.Repository.PaymentMethodRepository;
import com.timeco.application.Repository.PurchaseOrderRepository;
import com.timeco.application.Repository.WalletRepository;
import com.timeco.application.Repository.WalletTransactionRepository;
import com.timeco.application.model.order.PaymentMethod;
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
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;
    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;
    @Autowired
    private WalletTransactionRepository walletTransactionRepository;

    @Override
    public PaymentMethod getPaymentMethod(String paymentMethodName) {

        return paymentMethodRepository.findByPaymentMethodName(paymentMethodName);
    }






}