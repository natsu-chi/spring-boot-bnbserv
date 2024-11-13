package com.chi.bnbserv.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chi.bnbserv.entity.AboutTitle;

@Repository
public interface AboutTitleRepo extends JpaRepository<AboutTitle, Integer> {
    Page<AboutTitle> findByActive(String active, Pageable pageable);
}
