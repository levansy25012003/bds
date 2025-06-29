package com.example.bds.service;

import com.example.bds.dto.rep.WishlistRepDto;
import com.example.bds.model.DanhSachYeuThich;

import java.util.List;

public interface IDanhSachYeuThichService {
    public String toggleWishlist(Integer idProperty, Integer idUser);
    public List<WishlistRepDto> getAllWishlistByTaiKhoan(Integer id);
}
