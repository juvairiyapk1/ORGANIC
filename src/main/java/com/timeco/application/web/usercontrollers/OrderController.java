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
import java.util.*;

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







    @GetMapping("/userOrder")
    public String showUserOrder(Model model, Principal principal) {
        String userName = principal.getName();
        User user = userRepository.findByEmail(userName);

        if (user != null) {
            List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.findByUser(user);

            if (purchaseOrders != null && !purchaseOrders.isEmpty()) {
                List<OrderItem> allOrderItems = new ArrayList<>();

                for (PurchaseOrder order : purchaseOrders) {
                    List<OrderItem> orderItems = order.getOrderItems();
                    allOrderItems.addAll(orderItems);
                }

                Address address = purchaseOrders.get(0).getAddress(); // Assuming address is the same for all orders

                Collections.reverse(allOrderItems);
                int orderItemCount = allOrderItems.size();
                model.addAttribute("orderItems", allOrderItems);
                model.addAttribute("address", address);

                System.out.println("All Purchase Orders: " + purchaseOrders);
                System.out.println("All Order Items: " + allOrderItems);
                System.out.println("Address: " + address);
            }
        }
        return "UserOrder";
    }



    @PostMapping("/cancelOrderItem/{orderItemId}")
    public String cancelOrderItem(@PathVariable Long orderItemId) {
        purchaseOrderService.cancelOrderItem(orderItemId);

        return "redirect:/userOrder";
    }


    @PostMapping("/returnOrderItem/{orderItemId}")
    public String returnOrderItem(@PathVariable Long orderItemId){
        OrderItem orderItem=orderItemRepository.findById(orderItemId).orElse(null);
        if(orderItem != null && "Delivered".equals(orderItem.getOrderStatus()))
        {

         Product product=orderItem.getProduct();
         int returnItemQuantity=orderItem.getOrderItemCount();
         int currentProductCount=product.getQuantity();
         product.setQuantity(currentProductCount+returnItemQuantity);

            orderItem.setOrderStatus("Returned");
         User user=orderItem.getOrder().getUser();
         double productAmount;

         if (product.getDiscountedPrice()==null){

              productAmount=product.getPrice();
         }
         else {
             productAmount=product.getDiscountedPrice();
         }



         Wallet useWallet=user.getWallet();
         if(useWallet != null)
         {
             double currentWalletBalance=useWallet.getWalletAmount();
             useWallet.setWalletAmount(productAmount+currentWalletBalance);

             WalletTransaction walletTransaction=new WalletTransaction();
             walletTransaction.setWallet(user.getWallet());
             walletTransaction.setAmount(productAmount);
             walletTransaction.setTransactionTime(LocalDateTime.now());
             walletTransaction.setTransactionType("CREDIT");
             walletTransactionRepository.save(walletTransaction);
         }



            productRepository.save(product);
            orderItemRepository.save(orderItem);
            userRepository.save(user);
        }

      return "redirect:/userOrder";
    }


}
