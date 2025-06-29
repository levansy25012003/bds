package com.example.bds.dto.rep;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDetailDTO {
    private boolean success;
    private BatDongSanDTO postData;
    private List<VoterDTO> voters = new ArrayList<>();
    private List<CommentDTO> comments = new ArrayList<>();
}
