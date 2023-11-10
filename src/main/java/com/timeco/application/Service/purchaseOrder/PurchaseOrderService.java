package com.timeco.application.Service.purchaseOrder;

import com.timeco.application.model.order.OrderItem;
import com.timeco.application.model.order.PurchaseOrder;
import com.timeco.application.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PurchaseOrderService  {

    void addPurchaseOrder(PurchaseOrder purchaseOrder);


    List<OrderItem> convertPurchaseOrderToOrderItems(User user,PurchaseOrder purchaseOrder);

//    void deleteProduct(Long orderItemId);

    void updateOrderStatus(Long orderId, String newStatus);
}
