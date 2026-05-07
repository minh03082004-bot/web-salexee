package com.salexee.sale_xee;
import java.time.LocalDateTime;
public class OrderView {

    private String username;

    private Car car;

    private LocalDateTime orderDate;

    public OrderView(String username,
                     Car car,
                     LocalDateTime orderDate) {

        this.username = username;
        this.car = car;
        this.orderDate = orderDate;
    }

    public String getUsername() {
        return username;
    }

    public Car getCar() {
        return car;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }
}