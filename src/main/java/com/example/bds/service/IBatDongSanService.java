package com.example.bds.service;

import com.example.bds.dto.admin.BatDongSanResponseAdminDTO;
import com.example.bds.dto.admin.StatusRequestDTO;
import com.example.bds.dto.req.CommentReqDTO;
import com.example.bds.dto.req.CreatePostRequest;
import com.example.bds.dto.req.ExpireDTO;
import com.example.bds.model.BatDongSan;
import com.example.bds.model.TaiKhoan;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Optional;

public interface IBatDongSanService {
    Boolean createBatDongSan(CreatePostRequest request, Integer id);

    public Page<BatDongSan> getProductByOwn(String title, String propertyType, String status, Integer id, int page,
                                            int limit, String sort, String order);

    public Page<BatDongSan> getPostAll(String title, String propertyType, String listingType, String address,
                                       Integer minSize, Integer maxSize, Long minPrice, Long maxPrice,
                                       Integer bedroom, Integer bathroom, int page, int limit);

    public Optional<BatDongSan> findBaDongSanId(Integer id);
    public Boolean updateBatDongSan(CreatePostRequest req, Integer id);
    public Boolean deleteBatDongSan(Integer id);
    public Boolean createNewComment(Integer idTaiKhoan, CommentReqDTO req);
    public BatDongSanResponseAdminDTO getBatDongSanByAdmin(int limit, int page, String sort, String order, String status, Boolean isPublic);
    public boolean updateStatusAndPublicBatDongSan(StatusRequestDTO req, Integer id);
    public void increaseView(Integer id);

    Boolean createBatDongSanNhap(CreatePostRequest request, Integer id);

    public Page<BatDongSan> getDraftByOwn(String title, String propertyType, String status, Integer id, int page,
                                            int limit, String sort, String order);

    public Boolean updateDraftBatDongSan(CreatePostRequest req, Integer id, Boolean isDraft);
}
