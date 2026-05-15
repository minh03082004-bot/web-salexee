package com.salexee.sale_xee.controller;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.salexee.sale_xee.Car;
import com.salexee.sale_xee.CarRepository;
import com.salexee.sale_xee.CartItem;
import com.salexee.sale_xee.CartRepository;
import com.salexee.sale_xee.OrderItem;
import com.salexee.sale_xee.OrderRepository;
import com.salexee.sale_xee.OrderView;
import com.salexee.sale_xee.User;

import jakarta.servlet.http.HttpSession;
@Controller
public class CartController {

    @Autowired
    private CarRepository carRepository;
    @Autowired
private CartRepository cartRepository;
@Autowired
private OrderRepository orderRepository;
    @GetMapping("/cart/add/{id}")
public String addToCart(@PathVariable Long id,
                        HttpSession session) {

    User user = (User) session.getAttribute("user");

    if (user == null) {
        return "redirect:/login";
    }

    CartItem item = new CartItem();

    item.setUsername(user.getUsername());

    item.setCarId(id);

    cartRepository.save(item);

    return "redirect:/cars";
}

@GetMapping("/cart")
public String viewCart(HttpSession session,
                       Model model) {

    User user = (User) session.getAttribute("user");

    if (user == null) {
        return "redirect:/login";
    }

    List<CartItem> cartItems =
            cartRepository.findByUsername(user.getUsername());

    List<Car> cart = new ArrayList<>();

    double total = 0;

    for (CartItem item : cartItems) {

        Car car = carRepository
                .findById(item.getCarId())
                .orElse(null);

        if (car != null) {

            cart.add(car);

            total += car.getPrice();
        }
    }

    model.addAttribute("cart", cart);

    model.addAttribute("total", total);

    return "cart";
}

@GetMapping("/cart/remove/{id}")
public String removeFromCart(@PathVariable Long id,
                             HttpSession session) {

    User user = (User) session.getAttribute("user");

    if (user == null) {
        return "redirect:/login";
    }

    cartRepository.removeItem(
        user.getUsername(),
        id
    );

    return "redirect:/cart";
}
@GetMapping("/cart/checkout")
public String checkout(HttpSession session,
                       Model model,@RequestParam String phone) {

    User user = (User) session.getAttribute("user");

    if (user == null) {
        return "redirect:/login";
    }

    List<CartItem> cartItems =
            cartRepository.findByUsername(user.getUsername());

    if (cartItems.isEmpty()) {

        return "redirect:/cart";
    }
    for (CartItem item : cartItems) {

    OrderItem order = new OrderItem();

    order.setUsername(user.getUsername());

    order.setCarId(item.getCarId());
    order.setPhone(phone);

    order.setOrderDate(LocalDateTime.now());

    orderRepository.save(order);
}

    cartRepository.clearCart(user.getUsername());

    model.addAttribute(
            "message",
            "Đặt mua xe thành công!"
    );

    return "order-success";
}

@GetMapping("/my-orders")
public String myOrders(HttpSession session,
                       Model model) {

    User user = (User) session.getAttribute("user");

    if (user == null) {
        return "redirect:/login";
    }

    List<OrderItem> orderItems =
            orderRepository.findByUsername(
                    user.getUsername()
            );

    List<OrderView> orders = new ArrayList<>();

    for (OrderItem item : orderItems) {

        Car car = carRepository
                .findById(item.getCarId())
                .orElse(null);

        if (car != null) {

            orders.add(

                    new OrderView(
                            item.getUsername(),
                            car,
                            item.getOrderDate(),
                            item.getPhone()
                    )
            );
        }
    }

    model.addAttribute("orders", orders);

    return "my-orders";
}
}