package com.example.bds.dto.req;

import lombok.*;

@Getter
@Setter
public class VerifyOtpRequest {
    private String phone;
    private String code;
}
