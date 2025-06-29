package com.example.bds.repository;

import com.example.bds.model.GoiDichVu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoiDichVuRepository extends JpaRepository<GoiDichVu, Integer> {
}
