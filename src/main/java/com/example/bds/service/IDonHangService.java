package com.example.bds.service;

import com.example.bds.model.TaiKhoan;

public interface IDonHangService {
    public boolean createDonHang(TaiKhoan taiKhoan, Integer maGoiDichVu, Integer total);
}
