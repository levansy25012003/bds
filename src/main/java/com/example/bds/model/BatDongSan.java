package com.example.bds.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "batdongsan")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BatDongSan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maBatDongSan", nullable = false, unique = true)
    private Integer id;

    @Column(name = "tieuDe")
    private String tieuDe;

    @Column(name = "diaChi")
    private String diaChi;

    @Column(name = "xa")
    private String xa;

    @Column(name = "huyen")
    private String huyen;

    @Column(name = "tinh")
    private String tinh;

    @Column(name = "gia")
    private Long gia = 0L;

    @Column(name = "dongGia")
    private Integer dongGia = 0;

    @Column(name = "dienTich")
    private Integer dienTich = 0;

    @Column(name = "soTang")
    private Integer soTang = 0;

    @Column(name = "soPhongTam")
    private Integer soPhongTam = 0;

    @Column(name = "soPhongNgu")
    private Integer soPhongNgu = 0;

    @Column(name = "moTa", columnDefinition = "TEXT")
    private String moTa;

    @Column(name = "loaiBatDongSan")
    private String loaiBatDongSan;

    @Column(name = "loaiDanhSach")
    private String loaiDanhSach;

    @Column(name = "diemDanhGiaTB")
    private Float diemDanhGiaTB;

    @Column(name = "luotXem")
    private Integer luotXem = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "huongBanCong", columnDefinition = "ENUM('Đông', 'Tây', 'Nam', 'Bắc')")
    private Huong huongBanCong;

    @Enumerated(EnumType.STRING)
    @Column(name = "huong", columnDefinition = "ENUM('Đông', 'Tây', 'Nam', 'Bắc')")
    private Huong huong;

    @Column(name = "trangThai")
    private String trangThai;

    @Column(name = "noiThat")
    private Boolean noiThat = false;

    @Column(name = "congKhai")
    private Boolean congKhai = false;

    @Column(name = "xacThuc")
    private Boolean xacThuc = false;

    @Column(name = "ngayHetHan")
    private Timestamp ngayHetHan;

    @Column(name = "thoiGianHetHieuLucDayTin")
    private Timestamp thoiGianHetHieuLucDayTin;

    @Column(name = "hinhAnh", columnDefinition = "TEXT")
    private String hinhAnh;

    @Column(name = "the", columnDefinition = "TEXT")
    private String the;

    @Column(name = "createdAt", updatable = false, insertable = false)
    private Timestamp createdAt;

    @Column(name = "updatedAt", insertable = false)
    private Timestamp updatedAt;

    @Column(name = "banNhap")
    private Boolean banNhap = false;

    @ManyToOne
    @JoinColumn(name = "maGoiDichVu")
    @JsonBackReference
    private GoiDichVu goiDichVu;

    @ManyToOne
    @JoinColumn(name = "maTaiKhoan")
    @JsonBackReference
    private TaiKhoan taiKhoan;

    @OneToMany(mappedBy = "batDongSan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BinhLuan> binhLuans = new ArrayList<>();

    @OneToMany(mappedBy = "batDongSan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DanhGia> danhGias = new ArrayList<>();

    public enum Huong {
        Đông, Tây, Nam, Bắc
    }

}
