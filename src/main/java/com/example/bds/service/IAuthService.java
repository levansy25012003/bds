package com.example.bds.service;

import com.example.bds.dto.rep.ApiResponse;
import com.example.bds.dto.rep.AuthResponse;
import com.example.bds.dto.req.LoginEmailDTO;
import com.example.bds.dto.req.LoginPhoneDTO;
import com.example.bds.dto.req.ResetPasswordDTO;

import java.util.Map;

public interface IAuthService {
    AuthResponse loginWithEmail(LoginEmailDTO req);
    public AuthResponse loginWithPhone(LoginPhoneDTO req);
    public Map<String, Object> resetPasswordRequired(ResetPasswordDTO req);

    public ApiResponse resetByEmail(String email, String otp, String newPassword);
    public ApiResponse resetByPhone(String phone, String otp, String newPassword);
    public ApiResponse verifyByEmail(String email, String otp);
}
