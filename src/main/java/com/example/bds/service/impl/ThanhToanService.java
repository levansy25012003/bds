package com.example.bds.service.impl;

import com.example.bds.dto.rep.ThanhToanRepDTO;
import com.example.bds.dto.req.ThanhToanDTO;
import com.example.bds.model.TaiKhoan;
import com.example.bds.model.ThanhToan;
import com.example.bds.repository.TaiKhoanRepository;
import com.example.bds.repository.ThanhToanRepository;
import com.example.bds.service.IThanhToanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ThanhToanService implements IThanhToanService {

    private final TaiKhoanRepository taiKhoanRepository;
    private final ThanhToanRepository thanhToanRepository;


    public boolean createThanhToan(Integer maTaiKhoan, ThanhToanDTO req) {
        TaiKhoan taiKhoan = taiKhoanRepository.findById(maTaiKhoan).get();
        ThanhToan thanhToan = ThanhToan.builder()
                .soTien(req.getAmount())
                .trangThai(req.getStatus())
                .phuongThuc(req.getMethod())
                .maCode(req.getCode())
                .noiDung(req.getContent())
                .ngayTao(LocalDateTime.now())
                .taiKhoan(taiKhoan)
                .build();

        thanhToanRepository.save(thanhToan);
        return true;
    }

    @Override
    public List<ThanhToanRepDTO> getLichSuThanhToanThanhToan(int limit, int page, String sort, String order, TaiKhoan taiKhoan) {
        List<ThanhToan> thanhToans = thanhToanRepository.findByTaiKhoan_IdOrderByNgayTaoDesc(taiKhoan.getId());
        List<ThanhToanRepDTO> thanhToanRepDTOList = new ArrayList<>();
        for (ThanhToan item : thanhToans) {
            ThanhToanRepDTO thanhToanRepDTO = ThanhToanRepDTO.fromEntity(item);
            thanhToanRepDTOList.add(thanhToanRepDTO);
        }
        return thanhToanRepDTOList;
    }

}
