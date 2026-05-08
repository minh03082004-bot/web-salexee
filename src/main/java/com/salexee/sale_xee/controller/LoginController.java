package com.salexee.sale_xee.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @PostMapping("/")
public String login(@RequestParam String username,
                    @RequestParam String password) {

    if(username.equals("admin")) {
        return "redirect:/admin/cars";
    }

    return "redirect:/cars";
}
}