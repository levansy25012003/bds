package com.example.bds.dto.rep;

import com.example.bds.model.TinHetHan;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ExpiredHistory {
    private Integer id;
    private Integer idProperty;
    private Integer idUser;
    private String idInvoice;
    private Integer total;
    private Integer days;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @JsonProperty("rProperty")
    private PostRepDTO rProperty;
    @JsonProperty("rUser")
    private UserDTO rUser;

    public static ExpiredHistory fromEntity(TinHetHan tinHetHan) {
        return ExpiredHistory.builder()
                .id(tinHetHan.getId())
                .idProperty(tinHetHan.getBatDongSan().getId())
                .idUser(tinHetHan.getTaiKhoan().getId())
                .idInvoice(tinHetHan.getMaHoaDon())
                .total(tinHetHan.getTongSo())
                .days(tinHetHan.getSoNgay())
                .status(tinHetHan.getTrangThai())
                .createdAt(tinHetHan.getNgayTao())
                .updatedAt(tinHetHan.getNgayCapNhat())
                .rProperty(PostRepDTO.fromEntity(tinHetHan.getBatDongSan()))
                .rUser(UserDTO.fromEntity(tinHetHan.getTaiKhoan()))
                .build();
    }
}
