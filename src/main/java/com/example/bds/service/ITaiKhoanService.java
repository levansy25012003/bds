package com.example.bds.service;

import com.example.bds.dto.admin.TaiKhoanResponseAdminDTO;
import com.example.bds.exception.DataNotFoundException;
import com.example.bds.model.TaiKhoan;

import java.util.Optional;

public interface ITaiKhoanService {
    String login (String email, String password) throws Exception;
    TaiKhoan findById(Integer mataikhoan) throws DataNotFoundException;

    Optional<TaiKhoan> finByPhoneNumber(String phoneNumber) throws DataNotFoundException;

    int updatePhoneAndVerified(Integer id, String phone);

    public TaiKhoanResponseAdminDTO getAllTaiKhoanByAdmin(int limit, int page, String sort, String order, String fullname);

    public boolean updateRoleTaiKhoanByAdmin(Integer id, String role);
    public boolean buyGoiDichVu(TaiKhoan taiKhoan, Integer maGoiDichVu, Integer total);
    public boolean increaseSoDuTaiKhoan(Integer id, Integer amount);
}
