package com.springboot.blog.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Date;

@Getter
@Setter
public class BlogAPIException extends RuntimeException {
    private String fieldMessage;
    private HttpStatus httpStatus;

    public BlogAPIException(String fieldMessage, HttpStatus httpStatus) {
        // Creating generic exception handler
        super(String.format("%s", fieldMessage));
        this.fieldMessage = fieldMessage;
        this.httpStatus = httpStatus;
    }

}
