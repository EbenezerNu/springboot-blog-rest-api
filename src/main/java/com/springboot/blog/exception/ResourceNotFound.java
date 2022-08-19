package com.springboot.blog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFound extends RuntimeException {
    private String resourseName;
    private String fieldeName;
    private Long fieldValue;

    public ResourceNotFound(String resourseName, String fieldeName, Long fieldValue) {
        // Post not found with id : value
        super(String.format("%s with %s is not found \n: '%s'", resourseName, fieldeName, fieldValue));
        this.resourseName = resourseName;
        this.fieldeName = fieldeName;
        this.fieldValue = fieldValue;
    }

    public String getResourseName() {
        return resourseName;
    }

    public String getFieldeName() {
        return fieldeName;
    }

    public Long getFieldValue() {
        return fieldValue;
    }
}
