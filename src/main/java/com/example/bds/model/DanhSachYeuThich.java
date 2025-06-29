package com.example.bds.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "danhsachyeuthich")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DanhSachYeuThich {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maDsYeuThich")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "maBatDongSan", nullable = false)
    private BatDongSan batDongSan;

    @ManyToOne
    @JoinColumn(name = "maTaiKhoan", nullable = false)
    private TaiKhoan taiKhoan;
}
