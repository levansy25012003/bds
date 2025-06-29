package com.example.bds.specification;


import com.example.bds.model.BatDongSan;
import com.example.bds.model.GoiDichVu;
import com.example.bds.model.TaiKhoan;
import jakarta.persistence.criteria.*;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

@SuppressWarnings("serial")
@RequiredArgsConstructor
public class CustomBatDongSanSpecification implements Specification<BatDongSan> {

    @NonNull
    private String field;
    @NonNull
    private Object value;


    @Override
    public Predicate toPredicate(Root<BatDongSan> root,
                                 CriteriaQuery<?> query,
                                 CriteriaBuilder criteriaBuilder) {
        // Chỉ join nếu cần sort theo goiDichVu hoặc ép join luôn để hỗ trợ sort
        Join<BatDongSan, GoiDichVu> goiDichVuJoin = null;
        if (!query.getResultType().equals(Long.class)) { // Tránh join thừa khi count
            goiDichVuJoin = root.join("goiDichVu", JoinType.LEFT);
        }

        switch (field.toLowerCase()) {
            case "title":
                return criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("tieuDe")),
                        "%" + value.toString().toLowerCase() + "%"
                );
            case "propertytype":
                return criteriaBuilder.equal(root.get("loaiBatDongSan"), value.toString());
            case "status":
                return criteriaBuilder.equal(root.get("trangThai"), value.toString());
            case "id":
                Join<BatDongSan, TaiKhoan> taiKhoanJoin = root.join("taiKhoan", JoinType.LEFT);
                return criteriaBuilder.equal(taiKhoanJoin.get("id"), value);
            case "listingtype":
                return criteriaBuilder.equal(root.get("loaiDanhSach"), value.toString());
            case "address":
                return criteriaBuilder.equal(root.get("diaChi"), value.toString());
            case "bedroom":
                return criteriaBuilder.equal(root.get("soPhongNgu"), Integer.parseInt(value.toString()));
            case "bathroom":
                return criteriaBuilder.equal(root.get("soPhongTam"), Integer.parseInt(value.toString()));
            case "minprice":
                return criteriaBuilder.greaterThanOrEqualTo(root.get("gia"), Long.parseLong(value.toString()));
            case "maxprice":
                return criteriaBuilder.lessThanOrEqualTo(root.get("gia"), Long.parseLong(value.toString()));
            case "minsize":
                return criteriaBuilder.greaterThanOrEqualTo(root.get("dienTich"), Integer.parseInt(value.toString()));
            case "maxsize":
                return criteriaBuilder.lessThanOrEqualTo(root.get("dienTich"), Integer.parseInt(value.toString()));
            case "isverify":
                return criteriaBuilder.equal(root.get("congKhai"), value);
            case "province":
                return criteriaBuilder.equal(root.get("tinh"), value);
            case "district":
                return criteriaBuilder.equal(root.get("huyen"), value);
            case "ward":
                return criteriaBuilder.equal(root.get("xa"), value);
            case "isdraft":
                return criteriaBuilder.equal(root.get("banNhap"), value);
            case "notexpired":
                return criteriaBuilder.greaterThan(root.get("ngayHetHan"), LocalDateTime.now());
            default:
                return criteriaBuilder.conjunction(); // luôn đúng, không thêm điều kiện gì
        }
    }
//        root.join("goiDichVu", JoinType.LEFT);
//        if (field.equalsIgnoreCase("title")) {
//            return criteriaBuilder.like(
//                    criteriaBuilder.lower(root.get("tieuDe")),
//                    "%" + value.toString().toLowerCase() + "%"
//            );
//        }
//
//        if (field.equalsIgnoreCase("propertyType")) {
//            return criteriaBuilder.equal(root.get("loaiBatDongSan"), value.toString());
//        }
//
//        if (field.equalsIgnoreCase("status")) {
//            return criteriaBuilder.equal(root.get("trangThai"), value.toString());
//        }
//        if (field.equalsIgnoreCase("id")) {
//            Join<BatDongSan, TaiKhoan> taiKhoanJoin = root.join("taiKhoan");
//            return criteriaBuilder.equal(taiKhoanJoin.get("id"), value);
//        }
//
//        if (field.equalsIgnoreCase("listingType")) {
//            return criteriaBuilder.equal(root.get("loaiDanhSach"), value.toString());
//        }
//
//        if (field.equalsIgnoreCase("address")) {
//            return criteriaBuilder.equal(root.get("diaChi"), value.toString());
//        }
//
//        if (field.equalsIgnoreCase("bedroom")) {
//            return criteriaBuilder.equal(root.get("soPhongNgu"), Integer.parseInt(value.toString()));
//        }
//        if (field.equalsIgnoreCase("bathroom")) {
//            return criteriaBuilder.equal(root.get("soPhongTam"), Integer.parseInt(value.toString()));
//        }
//        if (field.equalsIgnoreCase("minPrice")) {
//            return criteriaBuilder.greaterThanOrEqualTo(root.get("gia"), Integer.parseInt(value.toString()));
//        }
//
//        if (field.equalsIgnoreCase("maxPrice")) {
//            return criteriaBuilder.lessThanOrEqualTo(root.get("gia"), Integer.parseInt(value.toString()));
//        }
//
//        if (field.equalsIgnoreCase("minSize")) {
//            return criteriaBuilder.greaterThanOrEqualTo(root.get("dienTich"), Integer.parseInt(value.toString()));
//        }
//
//        if (field.equalsIgnoreCase("maxSize")) {
//            return criteriaBuilder.lessThanOrEqualTo(root.get("dienTich"), Integer.parseInt(value.toString()));
//        }
////        if (field.equalsIgnoreCase("isPublic")) {
////            return criteriaBuilder.equal(root.get("congKhai"), value);
////        }
//        if (field.equalsIgnoreCase("isVerify")) {
//            return criteriaBuilder.equal(root.get("congKhai"), value);
//        }
//        return null;
//    }
}
