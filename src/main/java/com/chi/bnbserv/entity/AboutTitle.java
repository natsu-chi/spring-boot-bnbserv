package com.chi.bnbserv.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "about_title")
public class AboutTitle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "lan")
    private Integer lanId;

    @Column(name = "type_id")
    private Integer typeId;

    @Column(name = "title", length = 20)
    private String title;

    @Column(name = "content", length = 100)
    private String content;

    @Column(name = "active", length = 1)
    private String active = "Y";
}
