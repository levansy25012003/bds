package com.example.bds.dto.req;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class CreatePostRequest {
    private String description;
    private String address;
    private String province;
    private String title;

    private String district;
    private String ward;

    private Long price;
    private Integer priceUnit;
    private Integer idPricing;

    private List<String> tags;

    private Integer size;
    private Integer floor;
    private Integer bedroom;
    private Integer bathroom;

    private String propertyType;
    private String listingType;

    private String direction;
    private String balonyDirection;
    private Boolean isFurniture;
    private Date expiredDate;
    private List<String> media;
}
