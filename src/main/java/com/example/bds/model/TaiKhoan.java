package com.example.bds.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "taikhoan")
@Getter
@Setter
@AllArgsConstructor
public class TaiKhoan implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mataikhoan")
    private Integer id;

    @Column(name = "email")
    private String email;

    @Column(name = "emailXacThuc")
    private Boolean emailXacThuc = false;

    @Column(name = "hoVaTen")
    private String hoVaTen;

    @Column(name = "dienThoai")
    private String dienThoai;

    @Column(name = "dienThoaiXacThuc")
    private Boolean dienThoaiXacThuc = false;

    @Column(name = "matKhau")
    private String matKhau;

    @Column(name = "anhDaiDien")
    private String anhDaiDien;

    @Column(name = "soDu")
    private Integer soDu = 0;

    @Column(name = "diemTichLuy")
    private Integer diemTichLuy = 0;

    @ManyToOne
    @JoinColumn(name = "maGiaHienTai") // cột khóa ngoại
    private GoiDichVu maGiaHienTai;

    @OneToMany(mappedBy = "taiKhoan")
    private List<DanhSachYeuThich> danhSachYeuThiches;

    @OneToMany(mappedBy = "taiKhoan")
    private List<TinHetHan> tinHetHans;

    @Column(name = "maDatLaiMatKhau")
    private String maDatLaiMatKhau;

    @Column(name = "thoiGianHetMaMatKhau")
    private Timestamp thoiGianHetMaMatKhau;

    @Column(name = "ngayTao")
    private Timestamp ngayTao;

    @Column(name = "ngayCapNhat")
    private Timestamp ngayCapNhat;

    @Enumerated(EnumType.STRING)
    @Column(name = "vaiTro")
    private VaiTro vaiTro;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + vaiTro.name()));
    }

    @Override
    public String getPassword() {
        return matKhau;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public TaiKhoan(){
        this.vaiTro = VaiTro.THANHVIEN;
    }
    public enum VaiTro {
        THANHVIEN("Thành viên"),
        CHUBATDONGSAN( "Chủ bất động sản"),
        QUANTRIVIEN("Quản trị viên");

        private final String displayName;

        VaiTro(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

}
