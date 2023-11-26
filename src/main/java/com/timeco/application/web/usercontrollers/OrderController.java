package com.timeco.application.web.usercontrollers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import com.timeco.application.Repository.*;
import com.timeco.application.Service.cartService.CartService;
import com.timeco.application.Service.coupon.CouponService;
import com.timeco.application.Service.payment.PaymentService;
import com.timeco.application.Service.purchaseOrder.PurchaseOrderService;
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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class OrderController {

    @Autowired
    private CartService cartService;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PurchaseOrderService purchaseOrderService;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private CouponService couponService;

    @Autowired
    private WalletTransactionRepository walletTransactionRepository;

    @GetMapping("/success")
    public String success(){
        return "success";
    }




//    @PostMapping("/placeOrder")
//    @Transactional
//    public ResponseEntity<String> addPurchaseOrder(@RequestParam("addressId") Long addressId,
//                                                   @RequestParam("paymentMethodId") Long paymentMethodId,
//                                                   @RequestParam("totalAmount") double totalAmount,
//                                                   @RequestParam(value = "couponCode", required = false) String couponCode,
//                                                   Principal principal) throws RazorpayException {
//        Map<String, Object> response = new HashMap<>();
//        String username = principal.getName();
//        User user = userRepository.findByEmail(username);
//
//        try {
//            // Process Coupon
//            if (couponCode != null) {
//                Coupon coupon = couponRepository.findCouponByCouponCode(couponCode);
//                if (coupon != null) {
//                    coupon.incrementUsageCount();
//                    coupon.getUsers().add(user);
//                    user.getCoupons().add(coupon);
//
//                    couponRepository.save(coupon);
//                    userRepository.save(user);
//                }
//            }
//
//            // Fetch Cart Items
//            List<CartItem> cartItems = cartService.findCartItem(user);
//
//            // Create Purchase Order
//            Cart cart = cartRepository.findByUser(user);
//            PurchaseOrder purchaseOrder = new PurchaseOrder();
//            Optional<Address> addressOptional = addressRepository.findById(addressId);
//            addressOptional.ifPresent(purchaseOrder::setAddress);
//            Address address = addressOptional.orElse(null);
//
//            Coupon coupon = couponService.findCouponByCouponCode(couponCode);
//            if (coupon != null) {
//                purchaseOrder.setCoupon(coupon);
//            }
//
//            purchaseOrder.setOrderAmount(totalAmount);
//            purchaseOrder.setOrderedDate(LocalDateTime.now());
//            purchaseOrder.setUser(user);
//
//            PaymentMethod paymentMethod = paymentMethodRepository.findById(paymentMethodId).orElse(null);
//            if (paymentMethod != null) {
//                purchaseOrder.setPaymentMethod(paymentMethod);
//            }
//            System.out.println("5555555555555555555"+paymentMethodId);
//            response.put("isValid", true);
////            purchaseOrder.setOrderStatus("pending");
//            if(paymentMethodId==1) {
//                purchaseOrder.setOrderStatus("placed");
//                purchaseOrderRepository.save(purchaseOrder);
//                 response.put("isValid", true);
//
//            }
//            // Payment Processing
//            else if (paymentMethodId == 2) {
//               paymentService. handleRazorpayPayment(response, totalAmount, purchaseOrder, username, address);
//            } else if (paymentMethodId == 3) {
//                paymentService.handleWalletPayment(response, totalAmount, user, purchaseOrder);
//            }
//
//            // Clear Cart Items after successful order placement
////            cartItemRepository.deleteAll(cartItems);
//
//            // Convert Response to JSON
//            ObjectMapper objectMapper = new ObjectMapper();
//            String jsonResponse = objectMapper.writeValueAsString(response);
//            return ResponseEntity.ok(jsonResponse);
//        } catch (JsonProcessingException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing JSON response");
//        }
//    }
//
//    @PostMapping("/verifyPayment")
//    @ResponseBody
//    public ResponseEntity<Boolean> verifyPayment(@RequestParam("orderId") String orderId,
//                                                 @RequestParam("signature") String signature,
//                                                 @RequestParam("paymentId") String paymentId,
//                                                 @RequestParam("purchaseId") Long purchaseOrderId,
//                                                 Principal principal) throws RazorpayException {
//                        System.out.println("777777777"+purchaseOrderId);
//
//        RazorpayClient razorpay = new RazorpayClient("rzp_test_bgyWOWhe1pDEcz", "4mkCzKfIfbIXXJsHBnyuVTDI");
//
//        String secret = "4mkCzKfIfbIXXJsHBnyuVTDI";
//
//        JSONObject options = new JSONObject();
//        options.put("razorpay_order_id", orderId);
//        options.put("razorpay_payment_id", paymentId);
//        options.put("razorpay_signature", signature);
//
//        boolean status =  Utils.verifyPaymentSignature(options, secret);
//        if(status)
//        {
//            System.out.println("8888888888888888");
//            PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(purchaseOrderId).orElse(null);
//            if(purchaseOrder != null)
//            {
//                purchaseOrder.setOrderStatus("success");
//                purchaseOrder.setTransactionId(paymentId);
//                purchaseOrderRepository.save(purchaseOrder);
//                System.out.println("5555555555555555555555555555555550"+purchaseOrder.getPaymentMethod());
//                System.out.println(purchaseOrder.getOrderStatus());
//                System.out.println(purchaseOrder.getTransactionId());
//
//            }
//        }
//
//        return ResponseEntity.ok(status);
//    }



        @GetMapping("/userOrder")
        public String showUserOrder(Model model){
        List<OrderItem>orderItems=orderItemRepository.findAll();
        int orderItemCount = orderItems.size();
        model.addAttribute("orderItems", orderItems);
        return"UserOrder";
    }


    @PostMapping("/cancelOrderItem/{orderItemId}")
    public String cancelOrderItem(@PathVariable Long orderItemId) {
        purchaseOrderService.cancelOrderItem(orderItemId);

        return "redirect:/userOrder";
    }




}
