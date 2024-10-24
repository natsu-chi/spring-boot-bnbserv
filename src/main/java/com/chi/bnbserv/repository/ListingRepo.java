package com.chi.bnbserv.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chi.bnbserv.entity.Listing;

@Repository
public interface ListingRepo extends JpaRepository<Listing, Long> {
    List<Listing> findByCityId(Integer cityId);
    List<Listing> findByCityId(Integer cityId, Sort sort);
    Page<Listing> findByCityId(Integer cityId, Pageable pageable);
}
