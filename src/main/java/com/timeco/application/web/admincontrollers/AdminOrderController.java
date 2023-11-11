package com.timeco.application.web.admincontrollers;

import com.timeco.application.Repository.OrderItemRepository;
import com.timeco.application.Service.purchaseOrder.PurchaseOrderService;
import com.timeco.application.model.order.OrderItem;
import com.timeco.application.model.order.PurchaseOrder;
import com.timeco.application.model.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminOrderController {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private PurchaseOrderService purchaseOrderService;

    @GetMapping("/orderList")
    public String showOrderList(Model model){
        List<OrderItem>orderItems=orderItemRepository.findAll();
        model.addAttribute("orderItems",orderItems);
        return "OrderList";
    }



    @PostMapping("/updateOrderStatus")
    public String updateOrderStatus(@RequestParam("orderItemId") Long orderItemId,
                                    @RequestParam("newStatus") String newStatus) {
        System.out.println("5555555555555555555555"+orderItemId);
        purchaseOrderService.updateOrderStatus(orderItemId, newStatus);

        return "redirect:/admin/orderList";
    }



}
