package com.example.bds.dto.rep;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class AuthResponse {
    private boolean success;
    private String msg;
    private String accessToken;
}
