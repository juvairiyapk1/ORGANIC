package com.timeco.application.Service.payment;

import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.timeco.application.model.order.PaymentMethod;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService{

    @Value("${razorpay.api.key.id}")
    private String razorpayKey ;

    @Value("${razorpay.api.key.secret}")
    private String razorpaySecret;



    @Override
    public String createOrder(double totalAmount) {
        return null;
    }
}
