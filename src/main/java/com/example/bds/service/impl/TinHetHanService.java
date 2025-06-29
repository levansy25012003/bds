package com.example.bds.service.impl;

import com.example.bds.component.MaHoaDonUtil;
import com.example.bds.dto.rep.ExpiredHistory;
import com.example.bds.dto.req.ExpireDTO;
import com.example.bds.model.BatDongSan;
import com.example.bds.model.TaiKhoan;
import com.example.bds.model.TinHetHan;
import com.example.bds.repository.BatDongSanRepository;
import com.example.bds.repository.TaiKhoanRepository;
import com.example.bds.repository.TinHetHanRepository;
import com.example.bds.service.IBatDongSanService;
import com.example.bds.service.ITinHetHanService;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TinHetHanService implements ITinHetHanService {

    private final BatDongSanRepository batDongSanRepository;
    private final TinHetHanRepository tinHetHanRepository;
    private final TaiKhoanRepository taiKhoanRepository;
    @Override
    public boolean createTinHetHan(ExpireDTO req, TaiKhoan taiKhoan) {
        Optional<BatDongSan> bds = batDongSanRepository.findById(req.getIdPost());
        if (taiKhoan.getSoDu() < req.getTotal()) {
            return false;
        }
        TinHetHan tinHetHan = new TinHetHan();
        tinHetHan.setTongSo(req.getTotal());
        tinHetHan.setSoNgay(req.getDays());
        tinHetHan.setTrangThai("Thành công");
        tinHetHan.setBatDongSan(bds.get());
        tinHetHan.setTaiKhoan(taiKhoan);
        tinHetHan.setNgayTao(LocalDateTime.now());
        tinHetHan.setNgayCapNhat(LocalDateTime.now());
        tinHetHan.setMaHoaDon(MaHoaDonUtil.taoMaHoaDon());

        LocalDateTime expiredDate = LocalDateTime.now().plusDays(req.getDays());
        taiKhoanRepository.decreaseSoDu(req.getTotal(), taiKhoan.getId());
        batDongSanRepository.updateExpiredDateById(expiredDate, bds.get().getId());

        TinHetHan result = tinHetHanRepository.save(tinHetHan);
        if (result != null) {
            return true;
        }
        return false;
    }

    @Override
    public List<ExpiredHistory> getExpiredHistory(int limit, int page, String sort, String order, TaiKhoan taiKhoan) {

        List<TinHetHan> tinHetHans = tinHetHanRepository.findByTaiKhoan_Id(taiKhoan.getId());
        List<ExpiredHistory> expiredHistorys = new ArrayList<>();
        for (TinHetHan item : tinHetHans) {
            ExpiredHistory expiredHistory = ExpiredHistory.fromEntity(item);
            expiredHistorys.add(expiredHistory);
        }
        return expiredHistorys;
    }
}
