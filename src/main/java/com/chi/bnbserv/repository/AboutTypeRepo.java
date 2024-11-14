package com.chi.bnbserv.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chi.bnbserv.entity.AboutType;

@Repository
public interface AboutTypeRepo extends JpaRepository<AboutType, Integer> {

}