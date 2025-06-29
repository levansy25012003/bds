package com.example.bds.dto.admin;

import com.example.bds.model.BatDongSan;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class BatDongSanDTO {
    private Integer id;
    private String title;
    private Timestamp expiredDate;
    private String status;
    private Integer views;
    private Timestamp createdAt;
    private Boolean isPublic;

    @JsonProperty("rUser")
    private TaiKhoanDTO rUser;

    public static BatDongSanDTO fromEntity(BatDongSan entity) {
        return BatDongSanDTO.builder()
                .id(entity.getId())
                .title(entity.getTieuDe())
                .expiredDate(entity.getNgayHetHan())
                .status(entity.getTrangThai())
                .views(entity.getLuotXem())
                .createdAt(entity.getCreatedAt())
                .isPublic(entity.getCongKhai())
                .id(entity.getId())
                .rUser(TaiKhoanDTO.fromEntity(entity.getTaiKhoan()))
                .build();
    }
}
