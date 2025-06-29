package com.example.bds.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "goidichvu")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GoiDichVu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maGoiDichVu")
    private Integer id;

    @Column(name = "ten")
    private String name;

    @Column(name = "hienThiNgay")
    private Boolean isDisplayedImmediately = false;

    @Column(name = "hienThiNguoiBan")
    private Boolean isDisplayedSeller = false;

    @Column(name = "hienThiNutGoiDien")
    private Boolean isDisplayedPhoneBtn = false;

    @Column(name = "hienThiMoTa")
    private Boolean isDisplayedDescription = false;

    @Column(name = "kichThuocAnh")
    private String imageSize;

    @Column(name = "doUuTien")
    private Integer priority;

    @Column(name = "diemYeuCau")
    private Integer requiredScore;

    @Column(name = "diemYeuCauDenCapDoTiep")
    private Integer requiredScoreNextLevel;

    @Column(name = "gia")
    private Integer price;

    @Column(name = "ngayHetHan")
    private Integer expiredDay;

    @Column(name = "urlHinhAnh")
    private String imageUrl;

    @OneToMany(mappedBy = "maGiaHienTai")
    @JsonManagedReference
    private List<TaiKhoan> taiKhoans;

    @OneToMany(mappedBy = "goiDichVu")
    @JsonManagedReference
    private List<BatDongSan> danhSachBatDongSan;
}
