package com.example.bds.service;

import com.example.bds.dto.rep.CommentDTO;

import java.util.List;

public interface IBinhLuanService {
    List<CommentDTO> findCommentByMaBatDongSan(Integer id);
}
