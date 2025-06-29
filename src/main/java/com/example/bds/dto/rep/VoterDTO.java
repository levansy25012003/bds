package com.example.bds.dto.rep;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class VoterDTO {
    private Integer id;
    private Integer idUser;
    private Integer idProperty;
    private Integer star;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserDTO rUser;
}
