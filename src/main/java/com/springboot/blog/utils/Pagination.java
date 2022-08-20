package com.springboot.blog.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pagination<T> {

    private List<T> content;
    private int pageNo;
    private int pageSize;
    private long totalPages;
    private long totalElements;
    private boolean last;

}
