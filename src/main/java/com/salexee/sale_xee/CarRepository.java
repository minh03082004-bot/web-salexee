package com.salexee.sale_xee;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
      @Query("SELECT c FROM Car c WHERE " +
           "(:keyword IS NULL OR c.name LIKE %:keyword%) AND " +
           "(:brand IS NULL OR c.brand LIKE %:brand%) AND " +
           "(:minPrice IS NULL OR c.price >= :minPrice) AND " +
           "(:maxPrice IS NULL OR c.price <= :maxPrice)")
    Page<Car> searchCars(String keyword,
                         String brand,
                         Long minPrice,
                         Long maxPrice,
                         Pageable pageable);
      @Query("SELECT c.brand, COUNT(c) FROM Car c GROUP BY c.brand")
List<Object[]> countCarsByBrand();
}