package com.example.bds.controller;

import com.example.bds.component.MaHoaDonUtil;
import com.example.bds.component.VNPayUtil;
import com.example.bds.dto.req.CreatePostRequest;
import com.example.bds.dto.req.ThanhToanDTO;
import com.example.bds.model.TaiKhoan;
import com.example.bds.service.ITaiKhoanService;
import com.example.bds.service.IThanhToanService;
import com.example.bds.service.impl.VnpayService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment")
public class ThanhToanController {

    private final IThanhToanService thanhToanService;
    private final ITaiKhoanService taiKhoanService;
    @Value("${vnpay.client}")
    private String vnp_client;

    @Value("${vnpay.hashSecret}")
    private String vnp_hashSecret;
    private final VnpayService vnpayService;

    @PostMapping("/deposit")
    public ResponseEntity<?> depositMoney(@RequestBody Map<String, Integer> req,
                                          HttpServletRequest request,
                                          @AuthenticationPrincipal TaiKhoan taiKhoan) {
        int amount = req.get("amount");
        String uid = String.valueOf(taiKhoan.getId());

        String paymentUrl = vnpayService.createPaymentUrlVIP(amount, uid, request);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "paymentUrl", paymentUrl));
    }

    @GetMapping("/vnpay-return")
    public void handleVnpReturn(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, String> fields = new HashMap<>();

        // Lấy tất cả query parameters vào map
        Map<String, String[]> parameterMap = request.getParameterMap();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            if (entry.getValue().length > 0) {
                fields.put(entry.getKey(), entry.getValue()[0]);
            }
        }

        try {
            String orderInfo = fields.get("vnp_OrderInfo");
            Integer amount = Integer.parseInt(fields.get("vnp_Amount")) / 100;
            String[] parts = orderInfo.split("-");
            Integer idTaiKhoan = Integer.parseInt(parts[parts.length - 1]);

            ThanhToanDTO thanhToanDTO = ThanhToanDTO.builder()
                    .amount(amount)
                    .status("Thành công")
                    .method("VNPAY")
                    .code(MaHoaDonUtil.taoMaHoaDonVNP())
                    .content(orderInfo)
                    .build();
            boolean success = thanhToanService.createThanhToan(idTaiKhoan, thanhToanDTO);
            boolean result = taiKhoanService.increaseSoDuTaiKhoan(idTaiKhoan, amount);
            response.sendRedirect(vnp_client + "00");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(vnp_client + "02");
        }
    }

}
