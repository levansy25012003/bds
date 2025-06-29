package com.example.bds.service.impl;

import com.example.bds.component.MaHoaDonUtil;
import com.example.bds.dto.admin.BatDongSanDTO;
import com.example.bds.dto.admin.BatDongSanResponseAdminDTO;
import com.example.bds.dto.admin.StatusRequestDTO;
import com.example.bds.dto.rep.Pagination;
import com.example.bds.dto.req.CommentReqDTO;
import com.example.bds.dto.req.CreatePostRequest;
import com.example.bds.dto.req.ExpireDTO;
import com.example.bds.model.BatDongSan;
import com.example.bds.model.BinhLuan;
import com.example.bds.model.TaiKhoan;
import com.example.bds.model.TinHetHan;
import com.example.bds.repository.*;
import com.example.bds.service.IBatDongSanService;
import com.example.bds.specification.BatDongSanSpecification;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BatDongSanService implements IBatDongSanService {
    private final BatDongSanRepository batDongSanRepository;
    private final GoiDichVuRepository goiDichVuRepository;
    private final TaiKhoanRepository taiKhoanRepository;

    private final BinhLuanRepository binhLuanRepository;
    private final TinHetHanRepository tinHetHanRepository;

    @Override
    public Boolean createBatDongSan(CreatePostRequest req, Integer id) {
      try {
          BatDongSan bds = new BatDongSan();

          bds.setTieuDe(req.getTitle());
          bds.setMoTa(req.getDescription());
          bds.setDiaChi(req.getAddress());
          bds.setTinh(req.getProvince());
          bds.setHuyen(req.getDistrict());
          bds.setXa(req.getWard());
          bds.setGia(req.getPrice());
          bds.setDongGia(req.getPriceUnit());
          bds.setDienTich(req.getSize());
          bds.setSoTang(req.getFloor());
          bds.setSoPhongNgu(req.getBedroom());
          bds.setSoPhongTam(req.getBathroom());
          bds.setNoiThat(req.getIsFurniture());
          bds.setNgayHetHan(new Timestamp(req.getExpiredDate().getTime()));

          bds.setLoaiBatDongSan(req.getPropertyType());
          bds.setLoaiDanhSach(req.getListingType());

          if (req.getDirection() != null) {
              bds.setHuong(BatDongSan.Huong.valueOf(req.getDirection()));
          }
          if (req.getBalonyDirection() != null) {
              bds.setHuongBanCong(BatDongSan.Huong.valueOf(req.getBalonyDirection()));
          }

          if (req.getIdPricing() != null && req.getIdPricing() != 1) {
              bds.setCongKhai(true);
          } else {
              bds.setCongKhai(false);
          }
          bds.setTrangThai("Đang mở");
          if (req.getTags() != null) {
              ObjectMapper mapper = new ObjectMapper();
              String jsonTags = mapper.writeValueAsString(req.getTags());
              bds.setThe(jsonTags);
          }
          if (req.getMedia() != null) {
              ObjectMapper mapper = new ObjectMapper();
              String jsonMedia = mapper.writeValueAsString(req.getMedia());
              bds.setHinhAnh(jsonMedia);
          }
          bds.setXacThuc(false);
          bds.setLuotXem(0);
          // Thiết lập liên kết tài khoản và gói dịch vụ
          taiKhoanRepository.findById(id).ifPresent(bds::setTaiKhoan);
          if (req.getIdPricing() != null) {
              goiDichVuRepository.findById(req.getIdPricing())
                      .ifPresentOrElse(
                              bds::setGoiDichVu,
                              () -> goiDichVuRepository.findById(1)
                                      .ifPresent(bds::setGoiDichVu)
                      );
          } else {
              goiDichVuRepository.findById(1)
                      .ifPresent(bds::setGoiDichVu);
          }
          batDongSanRepository.save(bds);
          return true;
      } catch (Exception e) {
          e.printStackTrace();
          return false;
      }

    }

    @Override
    public Page<BatDongSan> getProductByOwn(String title, String propertyType, String status, Integer id, int page, int limit, String sort, String order) {
        Sort.Direction direction = order.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(Math.max(page - 1, 0), limit, Sort.by(direction, sort));

        Map<String, Object> filters = new HashMap<>();
        filters.put("title", title);
        filters.put("propertyType", propertyType);
        filters.put("status", status);
        filters.put("id", id);
        filters.put("isdraft", false); // ko lấy bản nháp

        Specification<BatDongSan> where = BatDongSanSpecification.buildWhere(filters);
        Page<BatDongSan> batDongSans = batDongSanRepository.findAll(where, pageable);

        return batDongSans;
    }

    @Override
    public Page<BatDongSan> getPostAll(String title, String propertyType, String listingType, String address,
                                       Integer minSize, Integer maxSize, Long minPrice, Long maxPrice,
                                       Integer bedroom, Integer bathroom, int page, int limit) {

        Sort sort = Sort.by(Sort.Direction.DESC, "goiDichVu.id").
                and(Sort.by(Sort.Direction.DESC, "createdAt"));
        Pageable pageable = PageRequest.of(Math.max(page - 1, 0), limit, sort);

        String[] addressParts = address != null ? address.split(",\\s*") : new String[0];
        Map<String, Object> filters = new HashMap<>();
        filters.put("title", title);
        filters.put("propertytype", propertyType);
        filters.put("listingtype", listingType);
//        filters.put("address", address);
        // ✅ Thêm điều kiện kiểm tra độ dài mảng trước khi thêm vào filters
        // ✅ Xử lý theo độ dài của addressParts:
        if (addressParts.length == 1) {
            filters.put("province", addressParts[0]);
        } else if (addressParts.length == 2) {
            filters.put("district", addressParts[0]);
            filters.put("province", addressParts[1]);
        } else if (addressParts.length >= 3) {
            filters.put("ward", addressParts[0]);
            filters.put("district", addressParts[1]);
            filters.put("province", addressParts[2]);
        }
        filters.put("minprice", minPrice);
        filters.put("maxprice", maxPrice);
        filters.put("minsize", minSize);
        filters.put("maxsize", maxSize);
        filters.put("bedroom", bedroom);
        filters.put("bathroom", bathroom);
        filters.put("isverify", true);
        filters.put("notexpired", true);
        Specification<BatDongSan> where = BatDongSanSpecification.buildWhere(filters);

        Page<BatDongSan> batDongSans = batDongSanRepository.findAll(where, pageable);
        return batDongSans;
    }

    @Override
    public Optional<BatDongSan> findBaDongSanId(Integer id) {
        Optional<BatDongSan> bds = batDongSanRepository.findById(id);
        return bds;
    }

    @Override
    public Boolean updateBatDongSan(CreatePostRequest req, Integer id) {
        Optional<BatDongSan> bdsOpt = findBaDongSanId(id);
        BatDongSan bds =  bdsOpt.get();
        try {
            bds.setTieuDe(req.getTitle());
            bds.setMoTa(req.getDescription());
            bds.setDiaChi(req.getAddress());
            bds.setTinh(req.getProvince());
            bds.setHuyen(req.getDistrict());
            bds.setXa(req.getWard());
            bds.setGia(req.getPrice());
            bds.setDongGia(req.getPriceUnit());
            bds.setDienTich(req.getSize());
            bds.setSoTang(req.getFloor());
            bds.setSoPhongNgu(req.getBedroom());
            bds.setSoPhongTam(req.getBathroom());
            bds.setNoiThat(req.getIsFurniture());


            bds.setLoaiBatDongSan(req.getPropertyType());
            bds.setLoaiDanhSach(req.getListingType());

            if (req.getDirection() != null) {
                bds.setHuong(BatDongSan.Huong.valueOf(req.getDirection()));
            }
            if (req.getBalonyDirection() != null) {
                bds.setHuongBanCong(BatDongSan.Huong.valueOf(req.getBalonyDirection()));
            }
            if (req.getMedia() != null) {
                ObjectMapper mapper = new ObjectMapper();
                String jsonMedia = mapper.writeValueAsString(req.getMedia());
                bds.setHinhAnh(jsonMedia);
            }

            batDongSanRepository.save(bds);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public Boolean updateDraftBatDongSan(CreatePostRequest req, Integer id, Boolean isDraft) {
        Optional<BatDongSan> bdsOpt = findBaDongSanId(id);
        BatDongSan bds =  bdsOpt.get();
        try {
            bds.setTieuDe(req.getTitle());
            bds.setMoTa(req.getDescription());
            bds.setDiaChi(req.getAddress());
            bds.setTinh(req.getProvince());
            bds.setHuyen(req.getDistrict());
            bds.setXa(req.getWard());
            bds.setGia(req.getPrice());
            bds.setDongGia(req.getPriceUnit());
            bds.setDienTich(req.getSize());
            bds.setSoTang(req.getFloor());
            bds.setSoPhongNgu(req.getBedroom());
            bds.setSoPhongTam(req.getBathroom());
            bds.setNoiThat(req.getIsFurniture());


            bds.setLoaiBatDongSan(req.getPropertyType());
            bds.setLoaiDanhSach(req.getListingType());

            if (req.getDirection() != null) {
                bds.setHuong(BatDongSan.Huong.valueOf(req.getDirection()));
            }
            if (req.getBalonyDirection() != null) {
                bds.setHuongBanCong(BatDongSan.Huong.valueOf(req.getBalonyDirection()));
            }
            bds.setBanNhap(isDraft);

            batDongSanRepository.save(bds);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
    @Override
    public Boolean deleteBatDongSan(Integer id) {
        Optional<BatDongSan> bds = findBaDongSanId(id);
        if (bds.isPresent()) {
            batDongSanRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public Boolean createNewComment(Integer idTaiKhoan, CommentReqDTO req) {

        Optional<TaiKhoan> taiKhoanOpt = taiKhoanRepository.findById(idTaiKhoan);
        Optional<BatDongSan> bdsOpt = batDongSanRepository.findById(Integer.parseInt(req.getIdProperty()));

        if (taiKhoanOpt.isPresent() && bdsOpt.isPresent()) {
            BinhLuan bl = new BinhLuan();
            bl.setNoiDung(req.getContent());
            bl.setTaiKhoan(taiKhoanOpt.get());
            bl.setBatDongSan(bdsOpt.get());
            bl.setNgayTao(LocalDateTime.now());
            bl.setNgayCapNhat(LocalDateTime.now());
            binhLuanRepository.save(bl);

            // Cộng điểm
            taiKhoanRepository.incrementScoreById(10, idTaiKhoan);
            return true;
        }
        return false;
    }

    @Override
    public BatDongSanResponseAdminDTO getBatDongSanByAdmin(int limit, int page, String sort, String order,
                                                           String status, Boolean isPublic) {
        Sort.Direction direction = order.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(direction, "createdAt"));

        Map<String, Object> filters = new HashMap<>();
        if (status != null && !status.isBlank()) {
            filters.put("status", status);
        }
        if (isPublic != null ) {
            filters.put("isPublic", isPublic);
        }
        Specification<BatDongSan> where = BatDongSanSpecification.buildWhere(filters);
        Page<BatDongSan> batDongSanPage = batDongSanRepository.findAll(where, pageable);

        List<BatDongSan> batDongSans = batDongSanPage.getContent();
        List<BatDongSanDTO> batDongSanDTOS = new ArrayList<>();
        for (BatDongSan item : batDongSans) {
            BatDongSanDTO batDongSanDTO = BatDongSanDTO.fromEntity(item);
            batDongSanDTOS.add(batDongSanDTO);
        }
        Pagination pagination = Pagination.builder()
                .limit(limit)
                .page(page)
                .count(batDongSanPage.getTotalElements())
                .totalPages(batDongSanPage.getTotalPages())
                .build();
        BatDongSanResponseAdminDTO result = BatDongSanResponseAdminDTO.builder()
                .success(true)
                .properties(batDongSanDTOS)
                .pagination(pagination)
                .build();
        return result;
    }

    @Override
    public boolean updateStatusAndPublicBatDongSan(StatusRequestDTO req, Integer id) {
        if (batDongSanRepository.findById(id).isPresent()) {
            batDongSanRepository.updateStatusAndPublic(req.getStatus(), req.getIsPublic(), id);
            return true;
        }
        return false;
    }

    @Override
    public void increaseView(Integer id) {
        batDongSanRepository.increaseLuotXem(id);
    }


    @Override
    public Boolean createBatDongSanNhap(CreatePostRequest req, Integer id) {
        try {
            BatDongSan bds = new BatDongSan();

            bds.setTieuDe(req.getTitle());
            bds.setMoTa(req.getDescription());
            bds.setDiaChi(req.getAddress());
            bds.setTinh(req.getProvince());
            bds.setHuyen(req.getDistrict());
            bds.setXa(req.getWard());
            bds.setGia(req.getPrice());
            bds.setDongGia(req.getPriceUnit());
            bds.setDienTich(req.getSize());
            bds.setSoTang(req.getFloor());
            bds.setSoPhongNgu(req.getBedroom());
            bds.setSoPhongTam(req.getBathroom());
            bds.setNoiThat(req.getIsFurniture());
            if (req.getExpiredDate() != null) {
                bds.setNgayHetHan(new Timestamp(req.getExpiredDate().getTime()));
            } else {
                bds.setNgayHetHan(new Timestamp(System.currentTimeMillis()));
            }


            bds.setLoaiBatDongSan(req.getPropertyType());
            bds.setLoaiDanhSach(req.getListingType());

            if (req.getDirection() != null) {
                bds.setHuong(BatDongSan.Huong.valueOf(req.getDirection()));
            }
            if (req.getBalonyDirection() != null) {
                bds.setHuongBanCong(BatDongSan.Huong.valueOf(req.getBalonyDirection()));
            }

            if (req.getIdPricing() != null && req.getIdPricing() != 1) {
                bds.setCongKhai(true);
            } else {
                bds.setCongKhai(false);
            }
            bds.setTrangThai("Đang mở");
            if (req.getTags() != null) {
                ObjectMapper mapper = new ObjectMapper();
                String jsonTags = mapper.writeValueAsString(req.getTags());
                bds.setThe(jsonTags);
            }
            if (req.getMedia() != null) {
                ObjectMapper mapper = new ObjectMapper();
                String jsonMedia = mapper.writeValueAsString(req.getMedia());
                bds.setHinhAnh(jsonMedia);
            }
            bds.setXacThuc(false);
            bds.setLuotXem(0);
            // Thiết lập liên kết tài khoản và gói dịch vụ
            taiKhoanRepository.findById(id).ifPresent(bds::setTaiKhoan);
            if (req.getIdPricing() != null) {
                goiDichVuRepository.findById(req.getIdPricing())
                        .ifPresentOrElse(
                                bds::setGoiDichVu,
                                () -> goiDichVuRepository.findById(1)
                                        .ifPresent(bds::setGoiDichVu)
                        );
            } else {
                goiDichVuRepository.findById(1)
                        .ifPresent(bds::setGoiDichVu);
            }
            bds.setBanNhap(true);
            batDongSanRepository.save(bds);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public Page<BatDongSan> getDraftByOwn(String title, String propertyType, String status, Integer id, int page, int limit, String sort, String order) {
        Sort.Direction direction = order.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(Math.max(page - 1, 0), limit, Sort.by(direction, sort));

        Map<String, Object> filters = new HashMap<>();
        filters.put("title", title);
        filters.put("propertyType", propertyType);
        filters.put("status", status);
        filters.put("id", id);
        filters.put("isdraft", true); // ko lấy bản nháp

        Specification<BatDongSan> where = BatDongSanSpecification.buildWhere(filters);
        Page<BatDongSan> batDongSans = batDongSanRepository.findAll(where, pageable);

        return batDongSans;
    }

}
