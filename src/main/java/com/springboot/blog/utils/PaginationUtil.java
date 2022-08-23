package com.springboot.blog.utils;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaginationUtil<T, G> {

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

    public Params fetchParams(int pageNo, int pageSize, String sortBy, String sortDir){
       Params params = new Params();
        params.setPageNo(pageNo);
        params.setPageSize(pageSize);
        params.setSortBy(sortBy);
        params.setSortDir(sortDir);

        return params;
    }
}
