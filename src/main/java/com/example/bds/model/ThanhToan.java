package com.example.bds.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "thanhtoan")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ThanhToan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maThanhToan")
    private Integer id;

    @Column(name = "soTien", nullable = false)
    private Integer soTien;

    @Column(name = "trangThai")
    private String trangThai;

    @Column(name = "phuongThuc")
    private String phuongThuc;

    @Column(name = "maCode")
    private String maCode;

    @Column(name = "noiDung")
    private String noiDung;

    @Column(name = "ngayTao")
    private LocalDateTime ngayTao;

    @Column(name = "ngayCapNhat")
    private LocalDateTime ngayCapNhat;

    @ManyToOne
    @JoinColumn(name = "maTaiKhoan", nullable = false)
    private TaiKhoan taiKhoan;

}
