package com.example.bds.repository;

import com.example.bds.model.TaiKhoan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface TaiKhoanRepository extends JpaRepository<TaiKhoan, Integer> {
    Optional<TaiKhoan> findByEmail(String email);
    @Query(value = """
           SELECT t.* 
           FROM taikhoan t
           LEFT JOIN goidichvu g ON t.maGiaHienTai = g.maGoiDichVu
           LEFT JOIN danhsachyeuthich d ON t.mataikhoan = d.mataikhoan 
           where t.email = :email
    """, nativeQuery = true)
    Optional<TaiKhoan> findByWithDetailByEmail(@Param("email") String email);

    Optional<TaiKhoan> findByDienThoai(String dienThoai);

    @Modifying
    @Transactional
    @Query(value = """ 
            UPDATE taikhoan 
            SET dienThoai = :phone, 
                dienThoaiXacThuc = true 
            WHERE maTaiKhoan = :id
    """,nativeQuery = true)
    int updatePhoneAndVerified(@Param("id") Integer id, @Param("phone") String phone);

    @Modifying
    @Query("UPDATE TaiKhoan t SET t.diemTichLuy = t.diemTichLuy + :points WHERE t.id = :id")
    void incrementScoreById(@Param("points") int points, @Param("id") int id);

    Page<TaiKhoan> findByHoVaTenContainingIgnoreCase(String fullname, Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE TaiKhoan t SET t.vaiTro = :role WHERE t.id = :id")
    int updateRoleById(@Param("id") int id,
                       @Param("role") TaiKhoan.VaiTro role);

    @Modifying
    @Transactional
    @Query("UPDATE TaiKhoan t SET t.soDu = t.soDu - :total WHERE t.id = :id")
    void decreaseSoDu(@Param("total") int total,
                      @Param("id") int id);
}
