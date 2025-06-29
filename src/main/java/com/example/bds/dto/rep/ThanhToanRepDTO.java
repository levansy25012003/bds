package com.example.bds.dto.rep;

import com.example.bds.dto.req.ThanhToanDTO;
import com.example.bds.model.ThanhToan;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ThanhToanRepDTO {
    private Integer id;
    private Integer idUser;
    private String idPayment;
    private Integer amount;
    private String method;
    private String status;
    private String code;
    private LocalDateTime createdAt;

    public static ThanhToanRepDTO fromEntity(ThanhToan thanhToan) {

        return ThanhToanRepDTO.builder()
                .id(thanhToan.getId())
                .idUser(thanhToan.getTaiKhoan().getId())
                .idPayment(thanhToan.getMaCode())
                .amount(thanhToan.getSoTien())
                .method(thanhToan.getPhuongThuc())
                .status(thanhToan.getTrangThai())
                .createdAt(thanhToan.getNgayTao())
                .build();
    }
}
