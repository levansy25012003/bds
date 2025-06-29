package com.example.bds.repository;

import com.example.bds.model.BinhLuan;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BinhLuanRepository extends JpaRepository<BinhLuan, Integer> {

    List<BinhLuan> findByBatDongSan_Id(Integer maBatDongSan);
}
