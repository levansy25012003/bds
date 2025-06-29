package com.example.bds.dto.req;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
@Builder
public class ResetPasswordDTO {
    private String email;
    private String phone;
}
