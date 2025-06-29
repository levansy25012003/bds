package com.example.bds.controller;

import com.example.bds.dto.rep.AuthResponse;
import com.example.bds.dto.req.LoginEmailDTO;
import com.example.bds.dto.req.LoginGoogleDTO;
import com.example.bds.dto.req.LoginPhoneDTO;
import com.example.bds.dto.req.LoginRequestDTO;
import com.example.bds.exception.DataNotFoundException;
import com.example.bds.service.impl.AuthService;
import com.example.bds.service.impl.TaiKhoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final TaiKhoanService taiKhoanService;
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) {
        try {
            String token = taiKhoanService.login(loginRequest.getEmail(), loginRequest.getPassword());
            return ResponseEntity.ok(token);
        }
        catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid email or password");
        }
    }

    @PostMapping("/google")
    public ResponseEntity<?> loginWithGoogle(@RequestBody LoginGoogleDTO req) {
        try {
            String accessToken = authService.loginWithGoogle(req);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("accessToken", accessToken);
            response.put("msg", "Đăng nhập thành công");

            return ResponseEntity.ok(response);
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "success", false,
                    "msg", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "msg", "Đã có lỗi xảy ra khi đăng nhập"
            ));
        }
    }

    @PostMapping("/login-email")
    public ResponseEntity<?> loginWithEmail(@RequestBody LoginEmailDTO req) {
        AuthResponse response = authService.loginWithEmail(req);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/login-phone")
    public ResponseEntity<?> loginWithPhone(@RequestBody LoginPhoneDTO req) {
        AuthResponse response = authService.loginWithPhone(req);
        return ResponseEntity.ok(response);
    }


}
