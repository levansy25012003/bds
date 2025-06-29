package com.example.bds.service.impl;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class DashboardService {

    public boolean getDashboardData(String daysStr, String type, String from, String to) {
        int days = daysStr != null ? Integer.parseInt(daysStr) : 220;
        String typeDate = "dd/MM/yyyy";
        if ("month".equalsIgnoreCase(type)) {
            typeDate = "MM/yyyy";
        }

        LocalDateTime start;
        LocalDateTime end;

        if (from != null && to != null) {
            start = LocalDate.parse(from, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay();
            end = LocalDate.parse(to, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atTime(23, 59, 59);
        } else {
            end = LocalDateTime.now();
            start = end.minusDays(days);
        }

        return false;
    }
}
