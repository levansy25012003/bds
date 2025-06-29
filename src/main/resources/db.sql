CREATE DATABASE bat_dong_san_vip;
drop database bat_dong_san_vip;

use  bat_dong_san_vip;

CREATE TABLE TAIKHOAN (
      maTaiKhoan INT AUTO_INCREMENT PRIMARY KEY,
      email VARCHAR(255),
      emailXacThuc BOOLEAN,
      hoVaTen VARCHAR(255),
      dienThoai VARCHAR(255),
      dienThoaiXacThuc BOOLEAN,
      matKhau VARCHAR(255),
      anhDaiDien VARCHAR(255),
      soDu INT default 0,
      diemTichLuy INT default 0,
      maGiaHienTai INT default 1,
      maDatLaiMatKhau VARCHAR(255),
      thoiGianHetMaMatKhau TIMESTAMP,
      ngayTao TIMESTAMP,
      ngayCapNhat TIMESTAMP,
      vaiTro ENUM('THANHVIEN', 'CHUBATDONGSAN', 'QUANTRIVIEN')
);
ALTER TABLE TAIKHOAN
    ADD CONSTRAINT fk_taikhoan_maGiaHienTai
        FOREIGN KEY (maGiaHienTai) REFERENCES goiDichVu(maGoiDichVu);

CREATE TABLE GOIDICHVU (
   maGoiDichVu INT AUTO_INCREMENT PRIMARY KEY,
   ten VARCHAR(255),
   hienThiNgay BOOLEAN DEFAULT FALSE,
   hienThiNguoiBan BOOLEAN DEFAULT FALSE,
   hienThiNutGoiDien BOOLEAN DEFAULT FALSE,
   hienThiMoTa BOOLEAN DEFAULT FALSE,
   kichThuocAnh VARCHAR(255),
   doUuTien INT,
   diemYeuCau INT,
   diemYeuCauDenCapDoTiep INT,
   gia INT,
   ngayHetHan INT,
   urlHinhAnh VARCHAR(255)
);

INSERT INTO goidichvu (maGoiDichVu, ten, kichThuocAnh, doUuTien, urlHinhAnh, diemYeuCau, diemYeuCauDenCapDoTiep, gia ,ngayHetHan)
VALUES(1, 'Thường', 'Nhỏ',  0, '/svg/badge-stock/base.svg',   0,   100, 0  ,   1),
      (2, 'Đồng', 'Nhỏ', 1,'/svg/badge-stock/bronze.svg',     100,  200,  100000,  3 ),
      (3, 'Bạc', 'Nhỏ', 2,'/svg/badge-stock/silver.svg',     200,   500, 200000,  5 ),
      (4, 'Vàng', 'Vữa', 3,'/svg/badge-stock/gold.svg',    500,    1000, 500000,10),
      (5, 'Kim Cương','Lơn', 4, '/svg/badge-stock/diamond.svg', 1000, 2000, 1000000, 20);
CREATE TABLE BATDONGSAN (
    maBatDongSan INT AUTO_INCREMENT PRIMARY KEY,
    tieuDe VARCHAR(255),
    diaChi VARCHAR(255),
    xa VARCHAR(255),
    huyen VARCHAR(255),
    tinh VARCHAR(255),
    gia INT default 0,
    dongGia INT default 0,
    dienTich INT default 0,
    soTang INT default 0,
    soPhongTam INT default 0,
    soPhongNgu INT default 0,
    moTa TEXT,
    loaiBatDongSan ENUM('Nhà', 'Đất', 'Chung cư') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
    loaiDanhSach ENUM('Bán', 'Cho thuê') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
    diemDanhGiaTB FLOAT,
    luotXem INT default 0,
    huongBanCong ENUM('Đông', 'Tây', 'Nam', 'Bắc') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
    huong ENUM('Đông', 'Tây', 'Nam', 'Bắc') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
    trangThai ENUM('Đang mở', 'Đang cập nhật', 'Đã bàn giao') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
    noiThat BOOLEAN default  false,
    congKhai BOOLEAN default  false,
    xacThuc BOOLEAN default false,
    ngayHetHan TIMESTAMP,
    thoiGianHetHieuLucDayTin TIMESTAMP DEFAULT '2025-03-01 00:09:59',
    hinhAnh TEXT,
    the TEXT,
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    maGoiDichVu INT,
    maTaiKhoan INT,

    FOREIGN KEY (maGoiDichVu) REFERENCES GOIDICHVU(maGoiDichVu),
    FOREIGN KEY (maTaiKhoan) REFERENCES TAIKHOAN(maTaiKhoan)
);
ALTER TABLE BATDONGSAN
    MODIFY COLUMN loaiBatDongSan VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE BATDONGSAN
    MODIFY COLUMN trangThai VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE batdongsan MODIFY COLUMN gia BIGINT;
ALTER TABLE BATDONGSAN
    ADD COLUMN banNhap BOOLEAN DEFAULT false;
ALTER TABLE BATDONGSAN
    MODIFY loaiDanhSach VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE DANHSACHYEUTHICH (
  maDsYeuThich INT AUTO_INCREMENT PRIMARY KEY,
  maBatDongSan INT,
  maTaiKhoan INT,

  FOREIGN KEY (maBatDongSan) REFERENCES BATDONGSAN(maBatDongSan),
  FOREIGN KEY (maTaiKhoan) REFERENCES TAIKHOAN(maTaiKhoan)
);

CREATE TABLE DANHGIA (
 maDanhGia INT AUTO_INCREMENT PRIMARY KEY,
 soSao INT DEFAULT 0,
 maBatDongSan INT,
 maTaiKhoan INT,

 FOREIGN KEY (maBatDongSan) REFERENCES BATDONGSAN(maBatDongSan),
 FOREIGN KEY (maTaiKhoan) REFERENCES TAIKHOAN(maTaiKhoan)
);

CREATE TABLE BINHLUAN (
  maBinhLuan INT AUTO_INCREMENT PRIMARY KEY,
  noiDung TEXT,
  maTaiKhoan INT,
  maBatDongSan INT,
  ngayTao DATETIME,
  ngayCapNhat DATETIME,

  FOREIGN KEY (maTaiKhoan) REFERENCES TAIKHOAN(maTaiKhoan),
  FOREIGN KEY (maBatDongSan) REFERENCES BATDONGSAN(maBatDongSan)
);

CREATE TABLE TINTUC (
    maTinTuc INT AUTO_INCREMENT PRIMARY KEY,
    tieuDe VARCHAR(255),
    anhDaiDien VARCHAR(255),
    noiDung TEXT,
    maTaiKhoan INT,
    ngayTao DATETIME,
    ngayCapNhat DATETIME,

    FOREIGN KEY (maTaiKhoan) REFERENCES TAIKHOAN(maTaiKhoan)
);

CREATE TABLE TINTHETHAN (
    maTinThetHan INT AUTO_INCREMENT PRIMARY KEY,
    tongSo INT DEFAULT 0,
    soNgay INT,
    trangThai ENUM('Thành công', 'Đang chờ', 'Thất bại') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
    maHoaDon VARCHAR(255),
    maTaiKhoan INT,
    maBatDongSan INT,
    ngayTao DATETIME,
    ngayCapNhat DATETIME,

    FOREIGN KEY (maTaiKhoan) REFERENCES TAIKHOAN(maTaiKhoan),
    FOREIGN KEY (maBatDongSan) REFERENCES BATDONGSAN(maBatDongSan)
);
ALTER TABLE TINTHETHAN
    MODIFY COLUMN trangThai VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE THANHTOAN (
   maThanhToan INT AUTO_INCREMENT PRIMARY KEY,
   soTien INT,
   trangThai ENUM('Thành công', 'Đang chờ', 'Thất bại') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
   phuongThuc VARCHAR(255),
   maCode VARCHAR(255),
   noiDung VARCHAR(255),

   ngayTao DATETIME,
   ngayCapNhat DATETIME,
   maTaiKhoan INT,
   FOREIGN KEY (maTaiKhoan) REFERENCES TAIKHOAN(maTaiKhoan)
);

ALTER TABLE THANHTOAN
    MODIFY COLUMN trangThai VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE DONHANG (
     maDonHang INT AUTO_INCREMENT PRIMARY KEY,
     maGoiDichVu INT,
     maTaiKhoan INT,
     tongSo INT,
     maHoaDon VARCHAR(255),
     trangThai ENUM('Thành công', 'Đang chờ', 'Thất bại') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
     FOREIGN KEY (maGoiDichVu) REFERENCES GOIDICHVU(maGoiDichVu),
     FOREIGN KEY (maTaiKhoan) REFERENCES TAIKHOAN(maTaiKhoan)
);
ALTER TABLE DONHANG
    MODIFY COLUMN trangThai VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

select * from BATDONGSAN;
SHOW COLUMNS FROM BATDONGSAN;
INSERT INTO TAIKHOAN (
    email,
    emailXacThuc,
    hoVaTen,
    dienThoai,
    dienThoaiXacThuc,
    matKhau,
    anhDaiDien,
    soDu,
    diemTichLuy,
    maGiaHienTai,
    maDatLaiMatKhau,
    thoiGianHetMaMatKhau,
    ngayTao,
    ngayCapNhat,
    vaiTro
) VALUES (
             'levansy25012003@gmail.com',
             true,
             'Nguyễn Văn A',
             '0905123456',
             true,
             '$2a$12$TYZ.bMbG0zuTI9LS0eh8NORveWDg9/YnHYAQ6NPG2l5nEgrjBDccW',  -- Nên mã hóa mật khẩu trong thực tế
             'avatar.jpg',
             1000000,
             50,
             1,
             NULL,
             NULL,
             CURRENT_TIMESTAMP,
             CURRENT_TIMESTAMP,
             'THANHVIEN'
         );
SELECT *
FROM taikhoan t
         LEFT JOIN goidichvu g ON t.maGiaHienTai = g.maGoiDichVu
         LEFT JOIN danhsachyeuthich d ON t.mataikhoan = d.mataikhoan
where t.email = :email;
select * from taikhoan;

UPDATE taikhoan SET dienThoai = :phone, dienThoaiXacThuc = true WHERE maTaiKhoan = :id;

select * from BATDONGSAN ;

select * from BINHLUAN;
select * from DANHGIA;
select * from DANHSACHYEUTHICH;
select * from TAIKHOAN;
SELECT * FROM batdongsan ORDER BY updatedAt DESC LIMIT 9;
SELECT COUNT(*) FROM batdongsan WHERE updatedAt IS NULL;
UPDATE TaiKhoan t SET t.vaiTro = :role WHERE t.maTaiKhoan = :id;

select * from tinthethan;
select * from DONHANG;
select * from GOIDICHVU;
select * from TAIKHOAN;
select * from THANHTOAN;
select * from batdongsan where tinh = "Thành Phố Đà Nẵng";

select * from batdongsan;