package com.chi.bnbserv.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "about_item")
public class AboutItem {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "type_id")
    private Integer typeId;

    @Column(name = "item_name", length = 50)
    private String itemName;

    @Column(name = "content", length = 500)
    private String content;

    @Column(name = "photo", length = 30)
    private String photo;

    @Column(name = "active", length = 1)
    private String active = "Y";
}
