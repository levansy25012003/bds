package com.example.bds.dto.rep;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class CommentDTO {
    private Integer id;
    private Integer idUser;
    private Integer idProperty;
    private Integer idParent; // Nếu là bình luận con
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserDTO commentator;
}
