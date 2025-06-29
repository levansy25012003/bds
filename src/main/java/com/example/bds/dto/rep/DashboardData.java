package com.example.bds.dto.rep;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class DashboardData {
    private int anonymous;
    private int registed;
    private List<PostStatDTO> posts;
    private long createdUser;
    private BigDecimal totalIncomes;
    private List<PricingReportDTO> pricingReport;
}
