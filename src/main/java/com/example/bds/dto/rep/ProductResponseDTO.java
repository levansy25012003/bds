package com.example.bds.dto.rep;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDTO<T> {

    private boolean success;
    private List<T> properties;
    private Pagination pagination;

}
