package com.example.bds.dto.rep;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Setter
public class DashboardResponseDTO {
    private boolean success;
    private DashboardData data;
}
