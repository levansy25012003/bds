package com.example.bds.dto.req;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ThanhToanDTO {
    private Integer amount;
    private String status;
    private String method;
    private String code;
    private String content;
}
