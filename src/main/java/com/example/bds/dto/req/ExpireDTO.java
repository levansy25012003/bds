package com.example.bds.dto.req;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExpireDTO {
    private Integer days;
    private Integer idPost;
    private Integer total;
}
