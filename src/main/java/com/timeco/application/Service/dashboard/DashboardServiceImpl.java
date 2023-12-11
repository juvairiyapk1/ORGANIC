package com.timeco.application.Service.dashboard;

import com.timeco.application.Repository.PurchaseOrderRepository;
import com.timeco.application.model.order.PurchaseOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Override
    public double calculateRevenue(String interval) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDate;
        LocalDateTime endDate;

        switch (interval) {
            case "weekly":
                startDate = now.with(DayOfWeek.MONDAY).truncatedTo(ChronoUnit.DAYS);
                endDate = now.with(DayOfWeek.SUNDAY).plusDays(1).truncatedTo(ChronoUnit.DAYS);
                break;
            case "monthly":
                startDate = now.withDayOfMonth(1).truncatedTo(ChronoUnit.DAYS);
                endDate = now.with(TemporalAdjusters.lastDayOfMonth()).plusDays(1).truncatedTo(ChronoUnit.DAYS);
                break;
            case "yearly":
                startDate = now.withDayOfYear(1).truncatedTo(ChronoUnit.DAYS);
                endDate = now.with(TemporalAdjusters.lastDayOfYear()).plusDays(1).truncatedTo(ChronoUnit.DAYS);
                break;
            default:
                throw new IllegalArgumentException("Invalid interval: " + interval);
        }

        List<PurchaseOrder> orders = purchaseOrderRepository.findByOrderedDateAfterAndOrderedDateBefore(startDate, endDate);

        System.out.println("orders==================="+orders);

        double revenue = orders.stream()
                .mapToDouble(PurchaseOrder::getOrderAmount)
                .sum();

        return revenue;
    }


}
