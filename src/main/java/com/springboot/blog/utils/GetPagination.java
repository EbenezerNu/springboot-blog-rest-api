package com.springboot.blog.utils;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetPagination<T, G> {

    public Pagination <G> fetch(Page<T> data, List<G> results) {

        Pagination<G> response = new Pagination<>();
        response.setContent(results);
        response.setPageNo(data.getNumber());
        response.setPageSize(data.getSize());
        response.setTotalPages(data.getTotalPages());
        response.setTotalElements(data.getTotalElements());
        response.setLast(data.isLast());

        return response;
    }
}
