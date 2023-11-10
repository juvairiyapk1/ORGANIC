package com.timeco.application.Service.purchaseOrder;

import com.timeco.application.Repository.CartItemRepository;
import com.timeco.application.Repository.OrderItemRepository;
import com.timeco.application.Repository.PurchaseOrderRepository;
import com.timeco.application.Service.cartService.CartService;
import com.timeco.application.model.cart.Cart;
import com.timeco.application.model.cart.CartItem;
import com.timeco.application.model.order.OrderItem;
import com.timeco.application.model.order.PurchaseOrder;
import com.timeco.application.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    @Transactional
    public void addPurchaseOrder(PurchaseOrder purchaseOrder) {
       purchaseOrderRepository.save(purchaseOrder);

//       cartService.deleteAllProduct(purchaseOrder.getUser());

    }

    @Override
    @Transactional
    public List<OrderItem> convertPurchaseOrderToOrderItems(User user,PurchaseOrder purchaseOrder) {
        List<OrderItem> orderItems = new ArrayList<>();
         Cart cartItem =user.getCart();
         List<CartItem>cartItems= cartItemRepository.findByCart(cartItem);


        for (CartItem item:cartItems) {
            OrderItem orderItem = new OrderItem();


            orderItem.setOrderItemCount(item.getQuantity());
            orderItem.setProduct(item.getProduct());
            orderItem.setOrder(purchaseOrder);



            orderItems.add(orderItem);
        }

        return orderItems;
    }


    @Override
    public void updateOrderStatus(Long orderId, String newStatus) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(orderId).orElse(null);
        if (purchaseOrder != null) {
            purchaseOrder.setOrderStatus(newStatus);
            purchaseOrderRepository.save(purchaseOrder);
        }
    }

}
