package com.chi.bnbserv.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chi.bnbserv.entity.ListingColumnBean;

@Repository
public interface ListingCsvBeanRepo extends JpaRepository<ListingColumnBean, String> {
    
}
