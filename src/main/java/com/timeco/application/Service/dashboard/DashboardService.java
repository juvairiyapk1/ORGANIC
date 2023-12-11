package com.timeco.application.Service.dashboard;

import org.springframework.stereotype.Service;

@Service
public interface DashboardService {


    double calculateRevenue(String interval);
}
