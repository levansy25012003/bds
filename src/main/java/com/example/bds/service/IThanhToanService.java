package com.example.bds.service;


import com.example.bds.dto.rep.ThanhToanRepDTO;
import com.example.bds.dto.req.ThanhToanDTO;
import com.example.bds.model.TaiKhoan;

import java.util.List;

public interface IThanhToanService {

    public boolean createThanhToan(Integer maTaiKhoan, ThanhToanDTO req);

    public List<ThanhToanRepDTO> getLichSuThanhToanThanhToan(int limit, int page, String sort, String order, TaiKhoan taiKhoan);
}
