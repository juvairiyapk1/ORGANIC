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
import com.timeco.application.model.order.*;
import com.timeco.application.model.product.Product;
import com.timeco.application.model.user.Address;
import com.timeco.application.model.user.User;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
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
    private ProductRepository productRepository;
    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private WalletTransactionRepository walletTransactionRepository;



    @PostMapping("/placeOrder")
    @Transactional
    public ResponseEntity<String> addPurchaseOrder(@RequestParam("addressId") Long addressId,
                                                   @RequestParam("paymentMethod") Long paymentMethodId,
                                                   @RequestParam("totalAmount") double totalAmount,
                                                   @RequestParam(value = "couponCode")String couponCode,
                                                   Principal principal) throws RazorpayException {

        System.out.println("444444444444444444444"+paymentMethodId);
        LocalDateTime currentDate = LocalDateTime.now();
        Address address = addressRepository.findById(addressId).orElse(null);
        PaymentMethod method = paymentMethodRepository.findById(paymentMethodId).orElse(null);
        Map<String, Object> response = new HashMap<>();
        String username = principal.getName();
//        User user = userRepository.findByEmail(username);


//        if(address==null || method==null)
//        {
//            redirectAttributes.addFlashAttribute("error","Address or payment method  not found");
//            return "redirect:/checkout";
//        }


        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setAddress(address);
        purchaseOrder.setPaymentMethod(method);
        purchaseOrder.setOrderedDate(currentDate);
        purchaseOrder.setOrderAmount(totalAmount);
        User user = userRepository.findByEmail(principal.getName());
        purchaseOrder.setUser(user);

        // Save the PurchaseOrder to the database



        List<OrderItem> orderItems = purchaseOrderService.convertPurchaseOrderToOrderItems(user, purchaseOrder);
        for (OrderItem orderItem : orderItems) {
            orderItemRepository.save(orderItem);

            Product product = orderItem.getProduct();
            int orderedQuantity = orderItem.getOrderItemCount();
            if (product.getQuantity() >= orderedQuantity) {
                System.out.println(product.getQuantity());
                product.setQuantity(product.getQuantity() - orderedQuantity);
                productRepository.save(product);
            }
        }

            // Payment method 1 logic
            // Set success response
            purchaseOrder.setOrderStatus("pending");
            response.put("isValid", true);
            purchaseOrderService.addPurchaseOrder(purchaseOrder);
//            cartItemRepository.deleteAll();



         if (paymentMethodId == 2) {
            RazorpayClient razorpay = new RazorpayClient("rzp_test_bgyWOWhe1pDEcz", "4mkCzKfIfbIXXJsHBnyuVTDI");

            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", totalAmount*100);
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "receipt#1");
            JSONObject notes = new JSONObject();
            notes.put("notes_key_1", "Tea, Earl Grey, Hot");
            orderRequest.put("notes", notes);

            Order order = razorpay.orders.create(orderRequest);
            response.put("isValid", false);
            response.put("orderId", order.get("id")); // Get orderId from Razorpay Order object
            response.put("amount", order.get("amount"));
            response.put("purchaseOrderId", purchaseOrder.getOrderId());
            response.put("email", username);
            assert address != null;
            response.put("username", address.getFirstName());
            response.put("contact", address.getPhoneNumber());

        }
        else if (paymentMethodId==3) {
            Wallet userWallet = walletRepository.findByUser(user);

            if (userWallet != null) {
                double walletBalance = userWallet.getWalletAmount();
                System.out.println("5555555555555555555555555"+walletBalance);
                double orderAmount=purchaseOrder.getOrderAmount();
                System.out.println("44444444444444444444"+orderAmount);

                if (walletBalance >= orderAmount) {
                    // Sufficient funds in the wallet
                    userWallet.withdraw(orderAmount);

                    // Record the wallet transaction
                    WalletTransaction walletTransaction = new WalletTransaction();
                    walletTransaction.setWallet(userWallet);
                    walletTransaction.setAmount(orderAmount);
                    walletTransaction.setTransactionType("DEBIT"); // Assuming 'DEBIT' for deduction, adjust accordingly
                    walletTransaction.setTransactionTime(LocalDateTime.now());
                    walletTransactionRepository.save(walletTransaction);
                    purchaseOrder.setOrderStatus("success");
                    purchaseOrderRepository.save(purchaseOrder);

                    // Continue with the rest of the order placement logic
                    response.put("isValid", true);
                    response.put("orderId", "Wallet payment"); // Adjust accordingly for wallet transactions
                    response.put("amount", orderAmount);
                    response.put("purchaseId", purchaseOrder.getOrderId());
                    response.put("email", username);
                    assert address != null;
                    response.put("username", address.getFirstName());
                    response.put("contact", address.getPhoneNumber());
                    cartItemRepository.deleteAll();
                } else {
                    // Insufficient funds in the wallet
                    response.put("isValid", false);
                    response.put("error", "Insufficient funds in the wallet");
                }
            }
           else {
                // User doesn't have a wallet
                response.put("isValid", true);
                response.put("error", "User does not have a wallet");
            }

        }




        if (couponCode != null) {
            Coupon coupon = couponRepository.findCouponByCouponCode(couponCode);
            if (coupon != null) {
                user.getCoupons().add(coupon);
                coupon.getUsers().add(user);

                userRepository.save(user);
                couponRepository.save(coupon);

            }
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString(response);
            return ResponseEntity.ok(jsonResponse);
        } catch (
                JsonProcessingException e) {
            // Handle the exception, e.g., log it and return an error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing JSON response");


        }



    }

    @PostMapping("/verifyPayment")

    public ResponseEntity<Boolean>verifyPayment(@RequestParam("orderId")String orderId,
                                                @RequestParam("signature")String signature,
                                                @RequestParam("paymentId")String paymentId,
                                                @RequestParam("purchaseOrderId")Long purchaseOrderId,
                                                Principal principal)throws RazorpayException
    {
        System.out.println("55555555555555555555555"+orderId);
        RazorpayClient razorpay=new RazorpayClient("rzp_test_bgyWOWhe1pDEcz","4mkCzKfIfbIXXJsHBnyuVTDI");

        String secret="4mkCzKfIfbIXXJsHBnyuVTDI";

        JSONObject options = new JSONObject();
        options.put("razorpay_order_id", orderId);
        options.put("razorpay_payment_id", paymentId);
        options.put("razorpay_signature", signature);

        boolean status= Utils.verifyPaymentSignature(options,secret);
        if (status) {
            PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(purchaseOrderId).orElse(null);
            if (purchaseOrder != null) {
                purchaseOrder.setOrderStatus("success");
                purchaseOrder.setTransactionId(paymentId);
                purchaseOrderRepository.save(purchaseOrder);
                cartItemRepository.deleteAll();
            }

        }
        return ResponseEntity.ok(status);

        }



//    @ResponseBody
//    @PostMapping("/payment")
//    public ResponseEntity<Map<String,Object>>paymentMethod(@ResponseBody Map<String,Object>formData, HttpSession session,Principal principal)
//    {
//        Map<String,Object>response=new HashMap<>();
//
//        String methodId=(String) formData.get("selectedPaymentMethod");
//        String amount=(String) formData.get("totalAmount");
//        Double grandTotal=amount.isBlank()?null:Double.parseDouble(amount);
//
//
//        String userName=principal.getName();
//        User user=userService.findUserByEmail(userName);
//        session.setAttribute("paymentMethodId",methodId);
//        session.setAttribute("finalAmount",grandTotal);
//
//        if(methodId.equals("wallet"))
//        {
//            if(purchaseOrderService.createOrderForWalletTransaction(grandTotal,user,session,principal))
//            {
//                response.put("success","order has been successfully");
//            }else{
//                response.put("message","Insufficient balance in wallet");
//            }
//            return new  ResponseEntity<>(response,HttpStatus.ACCEPTED);
//        }
//
//
//    }
//



}
