package com.example.bds.repository;

import com.example.bds.model.BatDongSan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface BatDongSanRepository extends JpaRepository<BatDongSan,Integer>, JpaSpecificationExecutor<BatDongSan> {
    @Override
    Optional<BatDongSan> findById(Integer integer);

    @Modifying
    @Transactional
    @Query("UPDATE BatDongSan b SET b.trangThai = :status, b.congKhai = :isPublic WHERE b.id = :id")
    int updateStatusAndPublic(@Param("status") String status,
                              @Param("isPublic") boolean isPublic,
                              @Param("id") Integer id);
    @Modifying
    @Transactional
    @Query("UPDATE BatDongSan b SET b.luotXem = b.luotXem + 1 WHERE b.id = :id")
    void increaseLuotXem(@Param("id") int id);

    @Modifying
    @Transactional
    @Query("UPDATE BatDongSan b SET b.ngayHetHan = :expiredDate WHERE b.id = :id")
    int updateExpiredDateById(@Param("expiredDate") LocalDateTime expiredDate, @Param("id") int id);
}
