package com.example.bds.dto.rep;


import com.example.bds.model.BatDongSan;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
public class BatDongSanDTO {
    private List<String> media;
    private List<String> tags;
    private int id;
    private String title;
    private String address;
    private String ward;
    private String district;
    private String province;
    private boolean isPublic;
    private String price;
    private String priceUnit;
    private int size;
    private int floor;
    private int bedroom;
    private int bathroom;
    private int averageStar;
    private String description;
    private String propertyType;
    private String listingType;
    private int idUser;
    private int idPricing;
    private int views;
    private String direction;
    private String balonyDirection;
    private boolean isFurniture;
    private Date expiredDate;
    private Date expiredBoost;
    private boolean verified;
    private String status;
    private Date createdAt;
    private Date updatedAt;

    @JsonProperty("rUser")
    private UserDTO rUser;
    public static BatDongSanDTO fromEntity(BatDongSan entity) {
        return BatDongSanDTO.builder()
                .id(entity.getId())
                .title(entity.getTieuDe())
                .address(entity.getDiaChi())
                .ward(entity.getXa())
                .district(entity.getHuyen())
                .province(entity.getTinh())
                // Chuyển đổi giá và đơn vị giá sang String
                .price(entity.getGia() == null ? "0" : entity.getGia().toString())
                .priceUnit(entity.getDongGia() == null ? "0" : entity.getDongGia().toString())
                .size(entity.getDienTich() == null ? 0 : entity.getDienTich())
                .floor(entity.getSoTang() == null ? 0 : entity.getSoTang())
                .bedroom(entity.getSoPhongNgu() == null ? 0 : entity.getSoPhongNgu())
                .bathroom(entity.getSoPhongTam() == null ? 0 : entity.getSoPhongTam())
                .averageStar(entity.getDiemDanhGiaTB() == null ? 0 : Math.round(entity.getDiemDanhGiaTB()))
                .description(entity.getMoTa())
                .propertyType(entity.getLoaiBatDongSan())
                // chuyển enum LoaiDanhSach sang String (ví dụ)
                .listingType(entity.getLoaiDanhSach() == null ? null : entity.getLoaiDanhSach())
                .idUser(entity.getTaiKhoan() == null ? 0 : entity.getTaiKhoan().getId())
                .idPricing(entity.getGoiDichVu() == null ? 0 : entity.getGoiDichVu().getId())
                .views(entity.getLuotXem() == null ? 0 : entity.getLuotXem())
                .direction(entity.getHuong() == null ? null : entity.getHuong().name())
                .balonyDirection(entity.getHuongBanCong() == null ? null : entity.getHuongBanCong().name())
                .isFurniture(entity.getNoiThat() == null ? false : entity.getNoiThat())
                .isPublic(entity.getCongKhai() == null ? false : entity.getCongKhai())
                .expiredDate(entity.getNgayHetHan() == null ? null : new Date(entity.getNgayHetHan().getTime()))
                .expiredBoost(entity.getThoiGianHetHieuLucDayTin() == null ? null : new Date(entity.getThoiGianHetHieuLucDayTin().getTime()))
                .verified(entity.getXacThuc() == null ? false : entity.getXacThuc())
                .status(entity.getTrangThai() == null ? null : entity.getTrangThai())
                .createdAt(entity.getCreatedAt() == null ? null : new Date(entity.getCreatedAt().getTime()))
                .updatedAt(entity.getUpdatedAt() == null ? null : new Date(entity.getUpdatedAt().getTime()))
                // Bạn cần xử lý 2 trường media và tags (nếu lưu kiểu String JSON, hoặc tách ra)
                // Ví dụ giả định hinhAnh lưu media dạng JSON String, còn the lưu tags dạng JSON String
                .media(parseJsonArrayString(entity.getHinhAnh()))
                .tags(parseJsonArrayString(entity.getThe()))
                .rUser(UserDTO.fromEntity(entity.getTaiKhoan()))
                .build();
    }

    private static List<String> parseJsonArrayString(String json) {
        if (json == null || json.isEmpty()) return Collections.emptyList();
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
