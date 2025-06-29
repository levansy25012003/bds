package com.example.bds.service;

import com.example.bds.dto.rep.ExpiredHistory;
import com.example.bds.dto.req.ExpireDTO;
import com.example.bds.model.TaiKhoan;

import java.util.List;

public interface ITinHetHanService {
    public boolean createTinHetHan(ExpireDTO req, TaiKhoan taiKhoan);
    public List<ExpiredHistory> getExpiredHistory(int limit, int page, String sort, String order, TaiKhoan taiKhoan);
}
