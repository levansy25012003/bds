package com.example.bds.dto.rep;

import com.example.bds.dto.req.GoiDichVuDto;
import com.example.bds.model.DanhSachYeuThich;
import com.example.bds.model.GoiDichVu;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class TaiKhoanResponse {
    private String email;
    private Integer id;
    private String fullname;
    private String role;
    private String phone;
    private Timestamp createdAt;
    private Integer balance;
    private Integer score;
    private String avatar;
    private boolean phoneVerified;
    private boolean emailVerified;
    private Integer currentPricingId;

    @JsonProperty("rPricing")
    private GoiDichVuDto rPricing;
    @JsonProperty("rWishlist")
    private List<WishlistRepDto> rWishlist;
}
