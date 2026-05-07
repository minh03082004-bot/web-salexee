package com.salexee.sale_xee;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
public interface CartRepository
        extends JpaRepository<CartItem, Long> {

    List<CartItem> findByUsername(String username);

    long countByUsername(String username);

    @Modifying
    @Transactional

    @Query("DELETE FROM CartItem c WHERE c.username = :username AND c.carId = :carId")

    void removeItem(@Param("username") String username,
                    @Param("carId") Long carId);
     @Modifying
    @Transactional
    @Query("DELETE FROM CartItem c WHERE c.username = :username")
    void clearCart(@Param("username") String username);
}