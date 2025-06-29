package com.example.bds.dto.admin;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatusRequestDTO {
    private Boolean isPublic;
    private String status;
}
