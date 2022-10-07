package com.springboot.blog.utils;

import lombok.Data;

@Data
public class Params {

    private int pageNo;
    private int pageSize;
    private String sortBy;
    private String sortDir;
    private String search;

}
