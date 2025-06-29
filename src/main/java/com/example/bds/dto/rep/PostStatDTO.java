package com.example.bds.dto.rep;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Setter
public class PostStatDTO {
    private String date;
    private long createdPost;

    public PostStatDTO(String date, long createdPost) {
        this.date = date;
        this.createdPost = createdPost;
    }
}
