package com.example.bds.repository;

import com.example.bds.model.DanhGia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DanhGiaRepository extends JpaRepository<DanhGia, Integer> {

    Optional<DanhGia> findByTaiKhoan_IdAndBatDongSan_Id(Integer maTaiKhoan, Integer maBatDongSan);

    @Query("SELECT AVG(d.soSao) FROM DanhGia d WHERE d.batDongSan.id = :maBatDongSan")
    Float findAverageSoSaoByBatDongSanId(@Param("maBatDongSan") Integer maBatDongSan);

    List<DanhGia> findByBatDongSan_Id(Integer maBatDongSan);
}
