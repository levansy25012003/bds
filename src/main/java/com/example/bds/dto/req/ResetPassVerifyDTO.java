package com.example.bds.dto.req;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class ResetPassVerifyDTO {
    private String email;
    private String phone;
    private String otp;
    private String password;
}
