package com.example.bds.service.impl;

import com.example.bds.dto.rep.BatDongSanDTO;
import com.example.bds.dto.rep.WishlistRepDto;
import com.example.bds.model.BatDongSan;
import com.example.bds.model.DanhSachYeuThich;
import com.example.bds.model.TaiKhoan;
import com.example.bds.repository.BatDongSanRepository;
import com.example.bds.repository.DanhSachYeuThichRepository;
import com.example.bds.repository.TaiKhoanRepository;
import com.example.bds.service.IDanhSachYeuThichService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DanhSachYeuThichService implements IDanhSachYeuThichService {

    private final DanhSachYeuThichRepository danhSachYeuThichRepository;
    private final BatDongSanRepository batDongSanRepository;
    private final TaiKhoanRepository taiKhoanRepository;

    @Override
    public String toggleWishlist(Integer idProperty, Integer idUser) {

        Optional<DanhSachYeuThich> danhSachYeuThich = danhSachYeuThichRepository.findByBatDongSan_IdAndTaiKhoan_id(idProperty, idUser);
        Optional<BatDongSan> bds = batDongSanRepository.findById(idProperty);
        Optional<TaiKhoan> tk = taiKhoanRepository.findById(idUser);

        if (danhSachYeuThich.isPresent()) {
            danhSachYeuThichRepository.delete(danhSachYeuThich.get());
            return "Đã xóa tin đăng yêu thích.";
        } else {
            DanhSachYeuThich danhSachYeuThichNew = new DanhSachYeuThich();
            danhSachYeuThichNew.setBatDongSan(bds.get());
            danhSachYeuThichNew.setTaiKhoan(tk.get());

            danhSachYeuThichRepository.save(danhSachYeuThichNew);
            return "Đã thêm tin đăng yêu thích.";
        }
    }

    @Override
    @Transactional
    public List<WishlistRepDto> getAllWishlistByTaiKhoan(Integer id) {
        List<DanhSachYeuThich> ds = danhSachYeuThichRepository.findByTaiKhoan_Id(id);
        List<WishlistRepDto> wishlistRepDtos = new ArrayList<>();
        for (DanhSachYeuThich item : ds) {
            WishlistRepDto wishlistRepDto = WishlistRepDto.builder()
                    .id(item.getId())
                    .idProperty(item.getBatDongSan().getId())
                    .idUser(item.getTaiKhoan().getId())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .rProperty(BatDongSanDTO.fromEntity(item.getBatDongSan()))
                    .build();
            wishlistRepDtos.add(wishlistRepDto);
        }

        return wishlistRepDtos;
    }
}
