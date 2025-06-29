package com.example.bds.dto.admin;

import com.example.bds.dto.rep.Pagination;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class TaiKhoanResponseAdminDTO {
    private boolean success;
    private List<TaiKhoanDTO> users;
    private Pagination pagination;
}
