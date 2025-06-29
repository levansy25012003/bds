package com.example.bds.service.impl;

import com.example.bds.dto.rep.UserDTO;
import com.example.bds.dto.rep.VoterDTO;
import com.example.bds.dto.req.RatingDTO;
import com.example.bds.model.BatDongSan;
import com.example.bds.model.DanhGia;
import com.example.bds.model.TaiKhoan;
import com.example.bds.repository.BatDongSanRepository;
import com.example.bds.repository.DanhGiaRepository;
import com.example.bds.repository.TaiKhoanRepository;
import com.example.bds.service.IDanhGiaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DanhGiaService implements IDanhGiaService {

    private final DanhGiaRepository danhGiaRepository;
    private final BatDongSanRepository batDongSanRepository;
    private final TaiKhoanRepository taiKhoanRepository;

    @Override
    @Transactional
    public boolean handleRating(RatingDTO req, TaiKhoan taiKhoan) {

        Optional<BatDongSan> bdsOpt = batDongSanRepository.findById(req.getIdProperty());
        BatDongSan bds = bdsOpt.get();
        Optional<DanhGia> danhGiaOpt = danhGiaRepository.findByTaiKhoan_IdAndBatDongSan_Id( taiKhoan.getId(),bds.getId());

        // Đã đánh giá rồi nhưng muốn đánh giá lại.
        if (danhGiaOpt.isPresent()) {
            DanhGia danhGia = danhGiaOpt.get();
            danhGia.setSoSao(req.getStar());
            danhGiaRepository.save(danhGia);
        } else {
            DanhGia danhGia = new DanhGia();
            danhGia.setSoSao(req.getStar());
            danhGia.setBatDongSan(bds);
            danhGia.setTaiKhoan(taiKhoan);

            danhGiaRepository.save(danhGia);
            // tăng 10 điểm cho người dùng
            taiKhoanRepository.incrementScoreById(10, taiKhoan.getId());
        }

        Float avgStar = danhGiaRepository.findAverageSoSaoByBatDongSanId(bds.getId());
        if (avgStar == null) return false;

        Float roundedAvg = Math.round(avgStar * 10) / 10.0f;
        bds.setDiemDanhGiaTB(roundedAvg);
        batDongSanRepository.save(bds);
        return true;
    }

    @Override
    @Transactional
    public List<VoterDTO> findRatingByMaBatDongSan(Integer id) {

        List<DanhGia> danhGias = danhGiaRepository.findByBatDongSan_Id(id);
        List<VoterDTO> voterDTOList = new ArrayList<>();
        for (DanhGia item : danhGias) {
            VoterDTO voterDTO = VoterDTO.builder()
                    .id(item.getId())
                    .idUser(item.getTaiKhoan().getId())
                    .idProperty(item.getBatDongSan().getId())
                    .star(item.getSoSao())
                    .rUser(UserDTO.fromEntity(item.getTaiKhoan()))
                    .build();
            voterDTOList.add(voterDTO);
        }

        return voterDTOList;
    }
}
