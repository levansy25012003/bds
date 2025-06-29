package com.example.bds.service;

import com.example.bds.dto.rep.VoterDTO;
import com.example.bds.dto.req.RatingDTO;
import com.example.bds.model.TaiKhoan;
import org.springframework.stereotype.Service;

import java.util.List;

public interface IDanhGiaService {
    boolean handleRating(RatingDTO req, TaiKhoan taiKhoan);
    public List<VoterDTO> findRatingByMaBatDongSan(Integer id);
}
