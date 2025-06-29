package com.example.bds.dto.req;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GoiDichVuDto {
    private Integer id;
    private String name;
    private Boolean isDisplayedImmediately;
    private Boolean isDisplayedSeller;
    private Boolean isDisplayedPhoneBtn;
    private Boolean isDisplayedDescription;
    private String imageSize;
    private Integer priority;
    private Integer requiredScore;
    private Integer requiredScoreNextLevel;
    private Integer price;
    private Integer expiredDay;
    private String imageUrl;
}