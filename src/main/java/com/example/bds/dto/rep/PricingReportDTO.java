package com.example.bds.dto.rep;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Setter
public class PricingReportDTO {

    private long idPricing;
    private long boughtPricing;

    public PricingReportDTO(long idPricing, long boughtPricing) {
        this.idPricing = idPricing;
        this.boughtPricing = boughtPricing;
    }
}
