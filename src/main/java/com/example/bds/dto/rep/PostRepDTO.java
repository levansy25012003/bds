package com.example.bds.dto.rep;

import com.example.bds.model.BatDongSan;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class PostRepDTO {
    private Integer id;
    private String title;
    private LocalDateTime expiredDate;

    public static PostRepDTO fromEntity(BatDongSan entity) {
        return PostRepDTO.builder()
                .id(entity.getId())
                .title(entity.getTieuDe())
                .expiredDate(entity.getNgayHetHan().toLocalDateTime())
                .build();
    }
}
