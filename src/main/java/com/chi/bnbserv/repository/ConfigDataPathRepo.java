package com.chi.bnbserv.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chi.bnbserv.entity.City;
import com.chi.bnbserv.entity.ConfigDataPath;

@Repository
public interface ConfigDataPathRepo extends JpaRepository<ConfigDataPath, Integer> {
    Optional<ConfigDataPath> findByCity(City city);
    Optional<ConfigDataPath> findByCity_Id(int cityId);
    Optional<ConfigDataPath> findByCity_NameIgnoreCase(String name);
}
