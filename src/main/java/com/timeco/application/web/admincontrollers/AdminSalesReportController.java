package com.timeco.application.web.admincontrollers;

import com.itextpdf.html2pdf.HtmlConverter;

import com.timeco.application.Repository.PurchaseOrderRepository;
import com.timeco.application.model.order.OrderItem;
import com.timeco.application.model.order.PurchaseOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminSalesReportController {

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @GetMapping("/salesReport")
    public String salesReport(Model model) {
        List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.findAll();

        // Create a list to store sales data
        List<Map<String, Object>> salesReportItems = createSalesReportItems(purchaseOrders);

        // Add the sales report data to the model
        model.addAttribute("salesReportItems", salesReportItems);

        return "salesReport";
    }

    @GetMapping("/salesReportFilter")
    public String filtering(@RequestParam(name = "startDate") String startDate,
                            @RequestParam(name = "endDate") String endDate,
                            Model model) {
        // Convert start and end date strings to LocalDateTime
        LocalDateTime startLocalDate = LocalDate.parse(startDate).atStartOfDay();
        LocalDateTime endLocalDate = LocalDate.parse(endDate).atTime(LocalTime.MAX);

        // Retrieve purchase orders within the specified date range (use your data access method)
        List<PurchaseOrder> filteredPurchaseOrders = purchaseOrderRepository.findByOrderedDateAfterAndOrderedDateBefore(startLocalDate, endLocalDate);

        // Process filtered purchase orders and create sales report items
        List<Map<String, Object>> salesReportItems = createSalesReportItems(filteredPurchaseOrders);

        // Add the filtered sales report data to the model
        model.addAttribute("salesReportItems", salesReportItems);

        // Add the start and end date to the model to display them on the page
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        return "salesReport";
    }

    private List<Map<String, Object>> createSalesReportItems(List<PurchaseOrder> purchaseOrders) {
        List<Map<String, Object>> salesReportItems = new ArrayList<>();

        for (PurchaseOrder order : purchaseOrders) {
            for (OrderItem orderItem : order.getOrderItems()) {
                String productName = orderItem.getProduct().getProductName();
                String category = orderItem.getProduct().getCategory().getName();
                int count = orderItem.getOrderItemCount();
                double amount = orderItem.getProduct().getPrice() * count;

                // Create a map for each sales report item
                Map<String, Object> reportItem = new HashMap<>();
                reportItem.put("productName", productName);
                reportItem.put("category", category);
                reportItem.put("count", count);
                reportItem.put("amount", amount);

                // Add the map to the list
                salesReportItems.add(reportItem);
            }
        }

        return salesReportItems;
    }


    @GetMapping("/downloadSalesReportPdf")
    @ResponseBody
    public byte[] downloadSalesReportPdf(HttpServletResponse response) {

        List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.findAll();

        // Retrieve sales data (you might get this from your database or another source)
        List<Map<String, Object>> salesReportItems = createSalesReportItems(purchaseOrders);

        try {
            String salesReportHtml = generateSalesReportHtml(salesReportItems);
            byte[] pdfBytes = generatePdf(salesReportHtml);

            // Set the content type and headers
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=sales_report.pdf");

            return pdfBytes;
        } catch (Exception e) {
            // Handle exception
            e.printStackTrace();
            return new byte[0];
        }
    }

    private byte[] generatePdf(String htmlContent) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            HtmlConverter.convertToPdf(htmlContent, outputStream);
            return outputStream.toByteArray();
        }
    }

    private String generateSalesReportHtml(List<Map<String, Object>> salesReportItems) {
        StringBuilder htmlBuilder = new StringBuilder();
        htmlBuilder.append("<html><body><h1>Sales Report</h1>");

        for (Map<String, Object> reportItem : salesReportItems) {
            String productName = (String) reportItem.get("productName");
            String category = (String) reportItem.get("category");
            int count = (int) reportItem.get("count");
            double amount = (double) reportItem.get("amount");

            htmlBuilder.append("<p>");
            htmlBuilder.append("Product: ").append(productName).append("<br>");
            htmlBuilder.append("Category: ").append(category).append("<br>");
            htmlBuilder.append("Count: ").append(count).append("<br>");
            htmlBuilder.append("Amount: ").append(amount).append("<br>");
            htmlBuilder.append("</p>");
        }

        htmlBuilder.append("</body></html>");

        return htmlBuilder.toString();
    }
}
