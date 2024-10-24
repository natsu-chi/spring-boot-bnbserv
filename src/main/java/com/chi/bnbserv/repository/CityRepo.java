package com.chi.bnbserv.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.chi.bnbserv.entity.City;
import com.chi.bnbserv.dto.CityDetailsDto;

public interface CityRepo extends JpaRepository<City, Integer>{
    @Query("SELECT new com.chi.bnbserv.dto.CityDetailsDto(a.id, a.name, b.name, c.name) " + 
           "FROM City a " + 
           "JOIN a.region b " + 
           "JOIN b.country c " + 
           "WHERE a.active = 'Y' " + 
           "AND b.active = 'Y' " + 
           "AND c.active = 'Y'")
    List<CityDetailsDto> fetchActiveCityDetails();

    @Query("SELECT new com.chi.bnbserv.dto.CityDetailsDto(a.id, a.name, b.name, c.name) " + 
           "FROM City a " + 
           "JOIN a.region b " + 
           "JOIN b.country c " + 
           "WHERE a.active = 'Y' " + 
           "AND b.active = 'Y' " + 
           "AND c.active = 'Y' " +
           "AND a.id = :id")
    List<CityDetailsDto> fetchActiveCityDetailsById(@Param("id") Integer id);
}
