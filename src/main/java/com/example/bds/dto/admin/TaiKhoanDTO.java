package com.example.bds.dto.admin;

import com.example.bds.model.TaiKhoan;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
public class TaiKhoanDTO {

    private Integer id;
    private String email;
    private boolean emailVerified;
    private String phone;
    private String fullname;
    private boolean phoneVerified;
    private String avatar;
    private String role;
    private Integer balance;
    private Integer score;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Integer currentPricingId;

    public static TaiKhoanDTO fromEntity(TaiKhoan entity) {
        TaiKhoanDTO taiKhoanDTO = TaiKhoanDTO.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .emailVerified(entity.getEmailXacThuc())
                .fullname(entity.getHoVaTen())
                .phone(entity.getDienThoai())
                .phoneVerified(entity.getDienThoaiXacThuc())
                .avatar(entity.getAnhDaiDien())
                .role(entity.getVaiTro().name())
                .balance(entity.getSoDu())
                .score(entity.getDiemTichLuy())
                .currentPricingId(entity.getMaGiaHienTai() != null ? entity.getMaGiaHienTai().getId() : 1)
                .createdAt(entity.getNgayTao())
                .updatedAt(entity.getNgayCapNhat())
                .build();
        return taiKhoanDTO;
    }
}
