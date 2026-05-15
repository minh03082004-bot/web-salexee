package com.salexee.sale_xee;
import java.time.LocalDateTime;
public class OrderView {

    private String username;

    private Car car;

    private LocalDateTime orderDate;
    private String phone;

    public OrderView(String username,
                     Car car,
                     LocalDateTime orderDate,String phone) {

        this.username = username;
        this.car = car;
        this.orderDate = orderDate;
        this.phone = phone;
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
    public String getPhone() {
    return phone;
}
}