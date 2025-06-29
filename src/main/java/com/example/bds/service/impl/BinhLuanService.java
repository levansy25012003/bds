package com.example.bds.service.impl;

import com.example.bds.dto.rep.CommentDTO;
import com.example.bds.dto.rep.UserDTO;
import com.example.bds.model.BinhLuan;
import com.example.bds.repository.BinhLuanRepository;
import com.example.bds.service.IBinhLuanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BinhLuanService implements IBinhLuanService {

    private final BinhLuanRepository binhLuanRepository;

    @Override
    public List<CommentDTO> findCommentByMaBatDongSan(Integer id) {

        List<BinhLuan> binhLuans = binhLuanRepository.findByBatDongSan_Id(id);
        List<CommentDTO> commentDtos = new ArrayList<>();
        for (BinhLuan item : binhLuans) {
            CommentDTO commentDTO = CommentDTO.builder()
                    .id(item.getId())
                    .idUser(item.getTaiKhoan().getId())
                    .idProperty(item.getBatDongSan().getId())
                    .idParent(null)
                    .content(item.getNoiDung())
                    .createdAt(item.getNgayTao())
                    .updatedAt(item.getNgayCapNhat())
                    .commentator(UserDTO.fromEntity(item.getTaiKhoan()))
                    .build();
            commentDtos.add(commentDTO);
        }
        return commentDtos;
    }
}
