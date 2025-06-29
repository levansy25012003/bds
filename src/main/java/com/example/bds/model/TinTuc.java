package com.example.bds.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tintuc")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TinTuc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maTinTuc")
    private Integer id;

    @Column(name = "tieuDe")
    private String tieuDe;

    @Column(name = "anhDaiDien")
    private String anhDaiDien;

    @Lob
    @Column(name = "noiDung", columnDefinition = "TEXT")
    private String noiDung;

    @ManyToOne
    @JoinColumn(name = "maTaiKhoan", nullable = false)
    private TaiKhoan taiKhoan;

    @Column(name = "ngayTao")
    private LocalDateTime ngayTao;

    @Column(name = "ngayCapNhat")
    private LocalDateTime ngayCapNhat;
}
