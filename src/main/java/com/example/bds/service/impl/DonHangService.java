package com.example.bds.service.impl;

import com.example.bds.component.MaHoaDonUtil;
import com.example.bds.model.DonHang;
import com.example.bds.model.GoiDichVu;
import com.example.bds.model.TaiKhoan;
import com.example.bds.repository.DonHangRepository;
import com.example.bds.repository.GoiDichVuRepository;
import com.example.bds.service.IDonHangService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DonHangService implements IDonHangService {

    private final GoiDichVuRepository goiDichVuRepository;
    private final DonHangRepository donHangRepository;

    public boolean createDonHang(TaiKhoan taiKhoan, Integer maGoiDichVu, Integer total) {
        Optional<GoiDichVu> goiDichVuOpt = goiDichVuRepository.findById(maGoiDichVu);
        if (goiDichVuOpt.isEmpty()) {
            return false;
        }
        DonHang donHang = DonHang.builder()
                .goiDichVu(goiDichVuOpt.get())
                .taiKhoan(taiKhoan)
                .tongSo(total)
                .maHoaDon(MaHoaDonUtil.taoMaHoaDon())
                .trangThai("Thành công")
                .build();
        donHangRepository.save(donHang);
        return true;
    }
}
