package com.salexee.sale_xee;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository
        extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByUsername(String username);
List<OrderItem> findAllByOrderByOrderDateDesc();
}