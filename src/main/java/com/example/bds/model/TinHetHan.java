package com.example.bds.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tinThetHan")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TinHetHan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maTinThetHan")
    private Integer id;

    @Column(name = "tongSo", nullable = false)
    private Integer tongSo = 0;

    @Column(name = "soNgay", nullable = false)
    private Integer soNgay;

    @Column(name = "trangThai")
    private String trangThai;

    @Column(name = "maHoaDon")
    private String maHoaDon;

    @ManyToOne
    @JoinColumn(name = "maTaiKhoan", nullable = false)
    private TaiKhoan taiKhoan;

    @ManyToOne
    @JoinColumn(name = "maBatDongSan", nullable = false)
    private BatDongSan batDongSan;

    @Column(name = "ngayTao")
    private LocalDateTime ngayTao;

    @Column(name = "ngayCapNhat")
    private LocalDateTime ngayCapNhat;


}
