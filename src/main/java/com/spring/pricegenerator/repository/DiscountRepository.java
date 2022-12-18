package com.spring.pricegenerator.repository;

import com.spring.pricegenerator.model.Discount;
import com.spring.pricegenerator.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {
    Discount findDiscountByCountry_CountryNameAndProduct(String countryName, Product product);
}
