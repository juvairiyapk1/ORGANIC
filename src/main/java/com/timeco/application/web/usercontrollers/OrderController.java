package com.timeco.application.web.usercontrollers;

import com.timeco.application.Repository.*;
import com.timeco.application.Service.cartService.CartService;
import com.timeco.application.Service.payment.PaymentService;
import com.timeco.application.Service.purchaseOrder.PurchaseOrderService;
import com.timeco.application.model.order.OrderItem;
import com.timeco.application.model.order.PaymentMethod;
import com.timeco.application.model.order.PurchaseOrder;
import com.timeco.application.model.product.Product;
import com.timeco.application.model.user.Address;
import com.timeco.application.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
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

    @GetMapping("/success")
    public String success(){
        return "success";
    }


    @PostMapping("/placeOrder")
    public String addPurchaseOrder(@RequestParam("addressId") Long addressId, @RequestParam("paymentMethodId") Long paymentMethodId,@RequestParam("totalAmount") double totalAmount, Principal principal, RedirectAttributes redirectAttributes)
    {
        LocalDate currentDate=LocalDate.now();
        Address address= addressRepository.findById(addressId).orElse(null);
        PaymentMethod method=paymentMethodRepository.findById(paymentMethodId).orElse(null);

        if(address==null || method==null)
        {
            redirectAttributes.addFlashAttribute("error","Address and payment method not found");
            return "redirect:/checkout";
        }

        PurchaseOrder purchaseOrder=new PurchaseOrder();
        purchaseOrder.setAddress(address);
        purchaseOrder.setPaymentMethod(method);
        purchaseOrder.setOrderedDate(currentDate);
        purchaseOrder.setOrderAmount(totalAmount);
        User user = userRepository.findByEmail(principal.getName());
        purchaseOrder.setUser(user);

        // Save the PurchaseOrder to the database
        purchaseOrderService.addPurchaseOrder(purchaseOrder);


        List<OrderItem>orderItems=purchaseOrderService.convertPurchaseOrderToOrderItems(user,purchaseOrder);
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

               cartItemRepository.deleteAll();



        return "success";
    }

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
