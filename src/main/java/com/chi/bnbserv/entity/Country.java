package com.chi.bnbserv.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "country")
public class Country {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "short_name", length = 50)
    private String shortName;
    
    @Column(name = "active", length = 1)
    private String active = "Y";

    @Column(name = "created_at")
    private LocalDate createAt;
}
