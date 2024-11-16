package com.chi.bnbserv.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class RequestAboutTitleDto {
    private Integer id;
    private String title;
    private String content;
}
