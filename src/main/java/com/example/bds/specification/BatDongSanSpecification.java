package com.example.bds.specification;

import com.example.bds.model.BatDongSan;
import org.springframework.data.jpa.domain.Specification;

import java.util.Map;

@SuppressWarnings("deprecation")
public class BatDongSanSpecification {

    public static Specification<BatDongSan> buildWhere(Map<String, Object> filters) {
        Specification<BatDongSan> where = null;

        for (Map.Entry<String, Object> entry : filters.entrySet()) {
            String field = entry.getKey();
            Object value = entry.getValue();

            if (value != null && !(value instanceof String && ((String) value).trim().isEmpty())) {
                CustomBatDongSanSpecification spec = new CustomBatDongSanSpecification(field, value);
                if (where == null) {
                    where = Specification.where(spec);
                } else {
                    where = where.and(spec);
                }
            }
        }
        return where != null ? where : Specification.where(null);
    }

}
