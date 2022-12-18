package com.spring.pricegenerator.repository;

import com.spring.pricegenerator.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {

    Country findCountryByCountryName(String countryName);
}
