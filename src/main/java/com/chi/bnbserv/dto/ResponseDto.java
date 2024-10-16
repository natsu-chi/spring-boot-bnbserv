package com.chi.bnbserv.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class ResponseDto {
    private String statusCode;
    private String statusMsg;
    private Object data;
}
