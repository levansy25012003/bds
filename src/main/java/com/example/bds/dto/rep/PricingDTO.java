package com.example.bds.dto.rep;

import com.example.bds.model.GoiDichVu;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
public class PricingDTO {
    private int id;
    private String name;
    private boolean isDisplayedImmediately;
    private boolean isDisplayedPhoneBtn;
    private boolean isDisplayedDescription;
    private boolean isDisplayedSeller;
    private int priority;
    private int requiredScore;
    private Integer requiredScoreNextLevel;
    private long price;
    private int expiredDay;
    private String imageSize;
    private String imageUrl;
    private Date createdAt;
    private Date updatedAt;

    public static PricingDTO fromEntity(GoiDichVu entity) {
        if (entity == null) return null;
        return PricingDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .isDisplayedImmediately(entity.getIsDisplayedImmediately())
                .isDisplayedPhoneBtn(entity.getIsDisplayedPhoneBtn())
                .isDisplayedDescription(entity.getIsDisplayedDescription())
                .isDisplayedSeller(entity.getIsDisplayedSeller())
                .priority(entity.getPriority())
                .requiredScore(entity.getRequiredScore())
                .requiredScoreNextLevel(entity.getRequiredScoreNextLevel())
                .price(entity.getPrice())
                .expiredDay(entity.getExpiredDay())
                .imageSize(entity.getImageSize())
                .imageUrl(entity.getImageUrl())
                .build();
    }
}
