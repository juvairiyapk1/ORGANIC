package com.timeco.application.web.admincontrollers;

import com.timeco.application.Repository.PaymentMethodRepository;
import com.timeco.application.Repository.ProductRepository;
import com.timeco.application.Repository.PurchaseOrderRepository;
import com.timeco.application.Repository.UserRepository;
import com.timeco.application.Service.dashboard.DashboardService;
import com.timeco.application.Service.payment.PaymentService;
import com.timeco.application.Service.purchaseOrder.PurchaseOrderService;
import com.timeco.application.model.order.OrderItem;
import com.timeco.application.model.order.PaymentMethod;
import com.timeco.application.model.order.PurchaseOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PurchaseOrderService purchaseOrderService;

//    @Autowired
//    private ReportGeneratorService reportGeneratorService;

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/adminHome")
    public String home(@RequestParam(name = "interval", defaultValue = "weekly") String interval, Model model) {
        List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.findAll();

        Map<String, Long> paymentCounts = new HashMap<>();

        // Count occurrences of each payment method name
        for (PurchaseOrder order : purchaseOrders) {
            PaymentMethod paymentMethod = order.getPaymentMethod();
            String paymentMethodName = (paymentMethod != null) ? paymentMethod.getPaymentMethodName() : "Unknown";

            // Increment count for the payment method name
            paymentCounts.put(paymentMethodName, paymentCounts.getOrDefault(paymentMethodName, 0L) + 1);
        }

        // Convert paymentCounts to a list of objects suitable for ApexCharts
        List<Map<String, Object>> chartData = new ArrayList<>();
        for (Map.Entry<String, Long> entry : paymentCounts.entrySet()) {
            Map<String, Object> dataPoint = new HashMap<>();
            dataPoint.put("x", entry.getKey());
            dataPoint.put("y", entry.getValue());
            chartData.add(dataPoint);
        }

//        users count
        Long totalUsers=userRepository.count();
        model.addAttribute("totalUsers",totalUsers);

        // Total Orders count
        int totalOrders = purchaseOrders.size();
        model.addAttribute("totalOrders", totalOrders);

        // Returned Orders count
        long returnedOrders = purchaseOrders.stream()
                .filter(order -> "Returned".equalsIgnoreCase(order.getOrderStatus()))
                .count();
        model.addAttribute("returnedOrders", returnedOrders);

        // Refunded Orders count
        long refundedOrders = purchaseOrders.stream()
                .filter(order -> "Refunded".equalsIgnoreCase(order.getOrderStatus()))
                .count();
        model.addAttribute("refundedOrders", refundedOrders);


        // Total Sales
        double totalSales = purchaseOrders.stream()
                .mapToDouble(PurchaseOrder::getOrderAmount)
                .sum();
        model.addAttribute("totalSales", totalSales);

        long canceledOrders = purchaseOrders.stream()
                .filter(order -> "Canceled".equalsIgnoreCase(order.getOrderStatus()))
                .count();
        model.addAttribute("canceledOrders", canceledOrders);



        model.addAttribute("chartData", chartData);

        // Update revenue and interval in the model
        updateRevenue(model, interval);

        // Aggregate sales data by month
        Map<YearMonth, Double> monthlySalesMap = purchaseOrders.stream()
                .collect(Collectors.groupingBy(
                        order -> YearMonth.from(order.getOrderedDate()),
                        Collectors.summingDouble(PurchaseOrder::getOrderAmount)
                ));

        // Extract months and corresponding sales data
        List<String> monthCategories = monthlySalesMap.keySet().stream()
                .map(YearMonth::toString)
                .collect(Collectors.toList());

        List<Double> monthlySalesData = monthlySalesMap.values().stream()
                .collect(Collectors.toList());



        model.addAttribute("monthCategories", monthCategories);
        model.addAttribute("monthlySalesData", monthlySalesData);



        return "adminHome";
    }

    private void updateRevenue(Model model, String interval) {

        double revenue = dashboardService.calculateRevenue(interval);

        model.addAttribute("revenue", revenue);
        model.addAttribute("interval", interval);
        System.out.println("revenue======================"+revenue);
        System.out.println("Intervel++==============="+interval);

    }


}
