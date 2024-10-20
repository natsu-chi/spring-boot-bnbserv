package com.chi.bnbserv.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data @AllArgsConstructor
@Getter @Setter
public class CityDetailsDto {
    private int id;
    private String city;
    private String region;
    private String country;
}
