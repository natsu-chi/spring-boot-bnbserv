package com.chi.bnbserv.entity;

import com.chi.bnbserv.util.csvConverter.NonEmptyStringConverter;
import com.chi.bnbserv.util.csvConverter.PriceStringConverter;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "listing")
public class ListingColumnBean extends CsvBean {
    @Id
    @CsvBindByName
    private String id;

    @CsvBindByName
    private String listing_url;

    @CsvBindByName
    private String last_scraped;

    @CsvBindByName
    private String name;

    @CsvBindByName
    private String description;

    @CsvBindByName
    private String picture_url;

    @CsvBindByName
    private String neighbourhood_cleansed;

    @CsvBindByName
    private String latitude;

    @CsvBindByName
    private String longitude;

    private String city_id;

    @CsvBindByName
    private String property_type;

    @CsvBindByName
    private String room_type;

    @CsvBindByName
    private String accommodates;

    @CsvBindByName
    private String bathrooms_text;

    @CsvBindByName
    private String bedrooms;

    @CsvCustomBindByName(converter = NonEmptyStringConverter.class)
    private String beds;

    @CsvBindByName
    private String amenities;

    @CsvCustomBindByName(converter = PriceStringConverter.class)
    private String price;

    @CsvBindByName
    private String has_availability;

    @CsvBindByName
    private String instant_bookable;

}
