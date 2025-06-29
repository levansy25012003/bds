package com.example.bds.dto.rep;

import com.example.bds.model.DanhSachYeuThich;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
public class WishlistRepDto {
    private Integer id;
    private Integer idUser;
    private Integer idProperty;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @JsonProperty("rProperty")
    private BatDongSanDTO rProperty;

    public static List<WishlistRepDto> fromEntity(List<DanhSachYeuThich> entitys) {

        List<WishlistRepDto> wishlistRepDtos = new ArrayList<>();
        for(DanhSachYeuThich item : entitys) {
            WishlistRepDto wishlistRepDto = WishlistRepDto.builder()
                    .id(item.getId())
                    .idUser(item.getTaiKhoan().getId())
                    .idProperty(item.getBatDongSan().getId())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            wishlistRepDtos.add(wishlistRepDto);
        }
        return wishlistRepDtos;
    }
}
