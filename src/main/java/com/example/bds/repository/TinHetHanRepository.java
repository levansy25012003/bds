package com.example.bds.repository;

import com.example.bds.model.TinHetHan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TinHetHanRepository extends JpaRepository<TinHetHan, Integer> {

    public List<TinHetHan> findByTaiKhoan_Id(int taiKhoanId);
}
