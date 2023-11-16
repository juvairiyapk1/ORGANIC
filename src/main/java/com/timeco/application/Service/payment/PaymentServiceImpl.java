package com.timeco.application.Service.payment;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Value("${razorpay.api.key.id}")
    private String razorpayKey;

    @Value("${razorpay.api.key.secret}")
    private String razorpaySecret;


//    private static final String RAZORPAY_KEY_ID = "rzp_test_bgyWOWhe1pDEcz";
//    private static final String RAZORPAY_KEY_SECRET = "4mkCzKfIfbIXXJsHBnyuVTDI";




}