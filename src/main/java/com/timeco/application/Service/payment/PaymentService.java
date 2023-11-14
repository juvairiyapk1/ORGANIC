package com.timeco.application.Service.payment;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public interface PaymentService {

//    String createOrder(double amount);

    String createOrder(double totalAmount);
}
