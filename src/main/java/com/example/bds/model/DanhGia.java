package com.example.bds.model;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "danhgia")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DanhGia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maDanhGia")
    private Integer id;

    @Column(name = "soSao", nullable = false)
    private Integer soSao = 0;

    @ManyToOne
    @JoinColumn(name = "maBatDongSan", nullable = false)
    private BatDongSan batDongSan;

    @ManyToOne
    @JoinColumn(name = "maTaiKhoan", nullable = false)
    private TaiKhoan taiKhoan;
}
