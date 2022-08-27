package com.springboot.blog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceIsEmpty extends RuntimeException {
    private String resourceName;
    private String parentName;

    public ResourceIsEmpty(String resourceName, String parentName) {
        // Post not found with id : value
        super(String.format("%s resource%s is Empty", resourceName, (parentName != null && parentName != "") ? (" for " + parentName) : ""));
        this.resourceName = resourceName;
        this.parentName = parentName;
    }

    public String getResourceName() {
        return resourceName;
    }
}
