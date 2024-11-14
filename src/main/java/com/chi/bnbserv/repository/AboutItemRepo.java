package com.chi.bnbserv.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chi.bnbserv.entity.AboutItem;

@Repository
public interface AboutItemRepo extends JpaRepository<AboutItem, Integer> {
    Page<AboutItem> findByActiveAndTypeId(String active, Integer typeId, Pageable pageable);
}
