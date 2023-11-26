package com.timeco.application.web.usercontrollers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import com.timeco.application.Repository.*;
import com.timeco.application.Service.cartService.CartService;
import com.timeco.application.Service.purchaseOrder.PurchaseOrderService;
import com.timeco.application.Service.userservice.UserService;
import com.timeco.application.model.cart.Cart;
import com.timeco.application.model.cart.CartItem;
import com.timeco.application.model.order.Coupon;
import com.timeco.application.model.order.OrderItem;
import com.timeco.application.model.order.PaymentMethod;
import com.timeco.application.model.order.PurchaseOrder;
import com.timeco.application.model.user.Address;
import com.timeco.application.model.user.User;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class PaymentController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private PurchaseOrderService purchaseOrderService;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private UserService userService;


    @PostMapping("/placeOrder")
    @ResponseBody
    public ResponseEntity<String> placeOrder(@RequestParam("addressId") Long addressId,
                                             @RequestParam("paymentMethod") Long paymentMethodId,
                                             @RequestParam("totalAmount") double totalAmount,
                                             @RequestParam(value = "couponCode", required = false) String couponCode,
                                             Principal principal) throws RazorpayException {
        Map<String, Object> response = new HashMap<>();
        String userName = principal.getName();
        User user = userRepository.findByEmail(userName);

        // 1. Coupon Processing
        if (couponCode != null) {
            Coupon coupon = couponRepository.findCouponByCouponCode(couponCode);

            if (coupon != null) {
                // Process coupon logic
                coupon.incrementUsageCount();
                coupon.getUsers().add(user);
                user.getCoupons().add(coupon);

                couponRepository.save(coupon);
                userRepository.save(user);
            }
        }

        Cart cart = cartRepository.findByUser(user);
        PurchaseOrder purchaseOrder = new PurchaseOrder();

        Optional<Address> addressOptional = addressRepository.findById(addressId);

        // 2. Check if address is present
        if (addressOptional.isPresent()) {
            Address address = addressOptional.get();
            purchaseOrder.setAddress(address);


            // Set other purchase order properties
            purchaseOrder.setOrderAmount(totalAmount);
            purchaseOrder.setOrderedDate(LocalDateTime.now());
            purchaseOrder.setUser(user);


            PaymentMethod paymentMethod = paymentMethodRepository.findById(paymentMethodId).orElse(null);
            purchaseOrder.setPaymentMethod(paymentMethod);
            purchaseOrder.setOrderStatus("pending");

            System.out.println("555555555555555555555"+paymentMethodId);
            // Response map initialization
            response.put("isValid", true);

            // Convert purchase order to order items
            purchaseOrderService.convertPurchaseOrderToOrderItems(user, purchaseOrder);

            // 3. Razorpay Order Creation
            if (paymentMethodId == 2) {
                try {
                    // Razorpay order creation logic
                    RazorpayClient razorpay = new RazorpayClient("rzp_test_bgyWOWhe1pDEcz", "4mkCzKfIfbIXXJsHBnyuVTDI");

                    JSONObject orderRequest = new JSONObject();
                    orderRequest.put("amount", totalAmount * 100);
                    orderRequest.put("currency", "INR");
                    orderRequest.put("receipt", "receipt#1");
                    JSONObject notes = new JSONObject();
                    notes.put("notes_key_1", "Tea, Earl Grey, Hot");
                    orderRequest.put("notes", notes);

                    Order order = razorpay.orders.create(orderRequest);
                    response.put("isValid", false);
                    response.put("orderId", order.get("id"));
                    response.put("amount", order.get("amount"));
                    response.put("purchaseId", purchaseOrder.getOrderId());
                    System.out.println("44444444444444444444444444"+purchaseOrder.getOrderId());
                    response.put("email", userName);
                    response.put("userName", address.getFirstName());
                    response.put("contact", address.getPhoneNumber());
                } catch (RazorpayException e) {
                    // Handle RazorpayException (e.g., log it and return an error response)
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing Razorpay order");
                }
            }

            try {
                // Convert response map to JSON
                ObjectMapper objectMapper = new ObjectMapper();
                String jsonResponse = objectMapper.writeValueAsString(response);
                return ResponseEntity.ok(jsonResponse);
            } catch (JsonProcessingException e) {
                // Handle JsonProcessingException (e.g., log it and return an error response)
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing JSON response");
            }
        } else {
            // Handle the case where the address is not present
            return ResponseEntity.badRequest().body("Invalid addressId");
        }
    }



    @PostMapping("/verifyPayment")
    @ResponseBody
    public ResponseEntity<Boolean>verifyPayment(@RequestParam("orderId")String orderId,
                                                @RequestParam("signature")String signature,
                                                @RequestParam("paymentId")String paymentId,
                                                @RequestParam("purchaseId")Long purchaseId,
                                                Principal principal)throws RazorpayException
    {
        RazorpayClient razorpay=new RazorpayClient("rzp_test_bgyWOWhe1pDEcz","4mkCzKfIfbIXXJsHBnyuVTDI");

        String secret="4mkCzKfIfbIXXJsHBnyuVTDI";

        JSONObject options = new JSONObject();
        options.put("razorpay_order_id", orderId);
        options.put("razorpay_payment_id", paymentId);
        options.put("razorpay_signature", signature);

        boolean status= Utils.verifyPaymentSignature(options,secret);
        if (status) {
            PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(purchaseId).orElse(null);
            if (purchaseOrder != null) {
                purchaseOrder.setOrderStatus("success");
                purchaseOrder.setTransactionId(paymentId);
                purchaseOrderRepository.save(purchaseOrder);
            }
        }
        return ResponseEntity.ok(status);

        }









}
