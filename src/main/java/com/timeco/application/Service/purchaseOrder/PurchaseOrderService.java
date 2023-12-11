package com.timeco.application.Service.purchaseOrder;

import com.timeco.application.model.cart.CartItem;
import com.timeco.application.model.order.OrderItem;
import com.timeco.application.model.order.PurchaseOrder;
import com.timeco.application.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Service
public interface PurchaseOrderService  {



    void addPurchaseOrder(PurchaseOrder purchaseOrder);



    List<OrderItem> convertPurchaseOrderToOrderItems(User user, PurchaseOrder purchaseOrder);



//    void deleteProduct(Long orderItemId);

  void updateOrderStatus(Long orderItemId, String newStatus);

    void cancelOrderItem(Long orderItemId);

//    List<PurchaseOrder> getAllOrders();
//
//    List<PurchaseOrder> getAllOrdersByStatus(String status);
//
//    int getNumberOfOrdersForDate(LocalDate lastDate);


//    boolean createOrderForWalletTransaction(Double grandTotal, User user, HttpSession session, Principal principal);
}
