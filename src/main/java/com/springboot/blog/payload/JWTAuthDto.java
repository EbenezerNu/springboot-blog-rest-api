package com.springboot.blog.payload;

import lombok.Data;

@Data
public class JWTAuthDto {

    private String accessToken;
    private String tokenType = "Bearer";

    public JWTAuthDto(String accessToken) {
        this.accessToken = accessToken;
    }
}
