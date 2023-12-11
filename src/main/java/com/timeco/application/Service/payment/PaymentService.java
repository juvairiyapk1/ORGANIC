package com.timeco.application.Service.payment;

import com.razorpay.RazorpayException;
import com.timeco.application.model.order.PaymentMethod;
import com.timeco.application.model.order.PurchaseOrder;
import com.timeco.application.model.user.Address;
import com.timeco.application.model.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface PaymentService {
    PaymentMethod getPaymentMethod(String paymentMethod);


}
