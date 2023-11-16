package com.timeco.application.Service.purchaseOrder;

import com.timeco.application.Repository.*;
import com.timeco.application.Service.cartService.CartService;
import com.timeco.application.Service.coupon.CouponService;
import com.timeco.application.Service.productservice.ProductService;
import com.timeco.application.model.cart.Cart;
import com.timeco.application.model.cart.CartItem;
import com.timeco.application.model.order.Coupon;
import com.timeco.application.model.order.OrderItem;
import com.timeco.application.model.order.PurchaseOrder;
import com.timeco.application.model.product.Product;
import com.timeco.application.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PurchaseOrderImpl implements PurchaseOrderService{

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
            orderItem.setOrder(purchaseOrder);
            orderItem.setOrderStatus("placed");



            orderItems.add(orderItem);
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

            orderItem.setOrderStatus("canceled");

            orderItemRepository.save(orderItem);

            Product product = orderItem.getProduct();
            int cancelledQuantity = orderItem.getOrderItemCount();
            product.setQuantity(product.getQuantity() + cancelledQuantity);
            productRepository.save(product);
        }
    }

//    @Override
//    public double calculateOrderTotal(String couponCode, double orderAmount){
//        double total=orderAmount;
//        if(couponService.isCouponValid(couponCode,orderAmount)){
//            Coupon coupon=couponRepository.findByCode(couponCode);
//            total = total * (1 - coupon.getPercentage() / 100);
//
//        }
//        return total;
//    }

}
