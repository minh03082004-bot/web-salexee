package com.salexee.sale_xee.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.salexee.sale_xee.User;
import com.salexee.sale_xee.UserRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    // =========================
    // MỞ TRANG LOGIN
    // =========================
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    // =========================
    // MỞ TRANG REGISTER
    // =========================
    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    // =========================
    // XỬ LÝ ĐĂNG KÝ
    // =========================
    @PostMapping("/register")
    public String register(
            @RequestParam String username,
            @RequestParam String password
    ) {

        User oldUser = userRepository.findByUsername(username);

        if (oldUser != null) {
            return "redirect:/register?error";
        }

        User user = new User(username, password, "USER");

        userRepository.save(user);

        return "redirect:/login";
    }

    // =========================
    // XỬ LÝ LOGIN
    // =========================
    @PostMapping("/login")
    public String login(
            @RequestParam String username,
            @RequestParam String password,
            HttpSession session
    ) {

        User user = userRepository.findByUsername(username);

        if (user != null && user.getPassword().equals(password)) {

            session.setAttribute("user", user);

            if (user.getRole().equals("ADMIN")) {
                return "redirect:/admin/cars";
            }

            return "redirect:/cars";
        }

        return "redirect:/login?error";
    }

    // =========================
    // LOGOUT
    // =========================
    @GetMapping("/logout")
    public String logout(HttpSession session) {

        session.invalidate();

         return "redirect:/cars";
        // return "redirect:/login";
    }

}