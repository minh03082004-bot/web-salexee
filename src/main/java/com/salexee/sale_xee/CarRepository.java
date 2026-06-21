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
//       @Query("SELECT c.brand, COUNT(c) FROM Car c GROUP BY c.brand")
// List<Object[]> countCarsByBrand();
@Query("""
SELECT c.brand,
       COUNT(c),
       SUM(CASE WHEN c.sold = true THEN 1 ELSE 0 END),
       SUM(CASE WHEN c.sold = false THEN 1 ELSE 0 END)
FROM Car c
GROUP BY c.brand
""")
List<Object[]> countCarsByBrand();
@Query("""
SELECT c.soldDate, COUNT(c)
FROM Car c
WHERE c.sold = true
GROUP BY c.soldDate
ORDER BY c.soldDate DESC
""")
List<Object[]> countSoldCarsByDate();
@Query("""
SELECT YEAR(c.soldDate),
       MONTH(c.soldDate),
       COUNT(c)
FROM Car c
WHERE c.sold = true
GROUP BY YEAR(c.soldDate),
         MONTH(c.soldDate)
ORDER BY YEAR(c.soldDate) DESC,
         MONTH(c.soldDate) DESC
""")
List<Object[]> countSoldCarsByMonth();
@Query("""
SELECT c.brand, COUNT(c)
FROM Car c
WHERE c.sold = true
GROUP BY c.brand
ORDER BY COUNT(c) DESC
""")
List<Object[]> topSellingBrands();
long countBySoldTrue();

long countBySoldFalse();
}