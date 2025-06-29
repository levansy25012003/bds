package com.example.bds.repository;

import com.example.bds.model.ThanhToan;
import com.example.bds.model.TinHetHan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThanhToanRepository extends JpaRepository<ThanhToan, Integer> {
    public List<ThanhToan> findByTaiKhoan_IdOrderByNgayTaoDesc(int taiKhoanId);
}
