package com.example.bds.repository;

import com.example.bds.model.DanhSachYeuThich;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DanhSachYeuThichRepository extends JpaRepository<DanhSachYeuThich, Integer> {

    Optional<DanhSachYeuThich> findByBatDongSan_IdAndTaiKhoan_id(Integer maBatDongSan, Integer maTaiKhoan);

    List<DanhSachYeuThich> findByTaiKhoan_Id(Integer maTaiKhoan);
}
