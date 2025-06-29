package com.example.bds.dto.rep;

import com.example.bds.model.TaiKhoan;
import lombok.*;

@Getter
@Setter
@Builder
public class UserDTO {
    private String phone;
    private String email;
    private String fullname;
    private String avatar;

    public static UserDTO fromEntity(TaiKhoan entity) {
        if (entity == null) return null;
        return UserDTO.builder()
                .phone(entity.getDienThoai())
                .email(entity.getEmail())
                .fullname(entity.getHoVaTen())
                .avatar(entity.getAnhDaiDien())
                .build();
    }
}
