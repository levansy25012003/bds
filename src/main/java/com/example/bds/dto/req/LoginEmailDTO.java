package com.example.bds.dto.req;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LoginEmailDTO {
    private String email;
    private String password;
}
