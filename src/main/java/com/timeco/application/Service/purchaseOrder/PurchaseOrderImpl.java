package com.timeco.application.Service.purchaseOrder;

import com.timeco.application.Repository.*;
import com.timeco.application.Service.addressService.AddressService;
import com.timeco.application.Service.cartService.CartService;
import com.timeco.application.Service.coupon.CouponService;
import com.timeco.application.Service.payment.PaymentService;
import com.timeco.application.Service.productservice.ProductService;
import com.timeco.application.Service.userservice.UserService;
import com.timeco.application.model.cart.Cart;
import com.timeco.application.model.cart.CartItem;
import com.timeco.application.model.order.*;
import com.timeco.application.model.product.Product;
import com.timeco.application.model.user.Address;
import com.timeco.application.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class PurchaseOrderImpl implements PurchaseOrderService{

    @Autowired
    private PaymentService paymentService;
    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository ;

    @Autowired
    private CouponService couponService;


    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AddressService addressService;

    @Override
    @Transactional
    public void addPurchaseOrder(PurchaseOrder purchaseOrder) {

       purchaseOrderRepository.save(purchaseOrder);

    }




    @Override
    public List<OrderItem> convertPurchaseOrderToOrderItems(User user,PurchaseOrder purchaseOrder) {


        List<OrderItem> orderItems = new ArrayList<>();
         Cart cartItem =user.getCart();
         List<CartItem>cartItems= cartItemRepository.findByCart(cartItem);


        for (CartItem item:cartItems) {
            OrderItem orderItem = new OrderItem();

            orderItem.setOrderItemCount(item.getQuantity());
            orderItem.setProduct(item.getProduct());
            System.out.println(item.getProduct());
            orderItem.setOrder(purchaseOrder);
            orderItem.setOrderStatus("Placed");



            orderItems.add(orderItem);
            orderItemRepository.save(orderItem);


            // Update product quantity
            Product product = item.getProduct();
            int newQuantity = product.getQuantity() - item.getQuantity();
            product.setQuantity(newQuantity);
            productRepository.save(product);


        }

        return orderItems;
    }


    @Override
    public void updateOrderStatus(Long orderItemId, String newStatus) {
        OrderItem orderItem = orderItemRepository.findById(orderItemId).orElse(null);
        if (orderItem != null) {
            orderItem.setOrderStatus(newStatus);
            orderItemRepository.save(orderItem);
        }
    }

    @Override
    public void cancelOrderItem(Long orderItemId) {
        Optional<OrderItem> optionalOrderItem = orderItemRepository.findById(orderItemId);

        if (optionalOrderItem.isPresent()) {
            OrderItem orderItem = optionalOrderItem.get();

            orderItem.setOrderStatus("Canceled");

            orderItemRepository.save(orderItem);

            Product product = orderItem.getProduct();
            int cancelledQuantity = orderItem.getOrderItemCount();
            product.setQuantity(product.getQuantity() + cancelledQuantity);
            productRepository.save(product);
        }
    }

//    @Override
//    public List<PurchaseOrder> getAllOrders() {
//        return purchaseOrderRepository.findAll();
//    }
//
//    @Override
//    public List<PurchaseOrder> getAllOrdersByStatus(String orderStatus) {
//        return purchaseOrderRepository.findAllByOrderStatus(orderStatus);
//    }
//
//    @Override
//    public int getNumberOfOrdersForDate(LocalDate date) {
//        return purchaseOrderRepository.findAllByOrderedDate(date).size();
//    }


}
