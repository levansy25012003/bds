package com.example.bds.dto.req;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@Builder
public class TaiKhoanUpdateDTO {
    private List<String> avatar;
    private Integer balance;
    private String email;
    private String fullname;
    private String phone;
    private String role;
    private Integer score;
}
