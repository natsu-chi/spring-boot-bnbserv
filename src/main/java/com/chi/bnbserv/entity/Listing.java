package com.chi.bnbserv.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "listing")
public class Listing {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "name", length = 200)
    private String name;

    @Lob
    @Column(name = "description")
    private String description; // 設定為 TEXT 類型

    @Column(name = "listing_url", length = 255)
    private String listingUrl;

    @Column(name = "picture_url", length = 255)
    private String pictureUrl;

    @Column(name = "latitude", precision = 17, scale = 15)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 18, scale = 15)
    private BigDecimal longitude;

    @Column(name = "city_id")
    private Integer cityId;

    @Column(name = "neighbourhood_cleansed", length = 50)
    private String neighbourhoodCleansed;

    @Column(name = "property_type", length = 50)
    private String propertyType;

    @Column(name = "room_type", length = 50)
    private String roomType;

    @Column(name = "host_id")
    private Long hostId;

    @Column(name = "bathrooms_text", length = 50)
    private String bathroomsText;

    @Column(name = "bedrooms")
    private Double bedrooms;

    @Column(name = "beds")
    private Double beds;

    @Column(name = "amenities", length = 2000)
    private String amenities;

    @Column(name = "accommodates")
    private Integer accommodates;

    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "has_availability", length = 1)
    private String hasAvailability;

    @Column(name = "instant_bookable", length = 1)
    private String instantBookable;

    @Column(name = "last_scraped")
    private LocalDate lastScraped;

}
