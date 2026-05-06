package com.salexee.sale_xee.controller;
//package com.example.demo.controller; // Chỗ này phải khớp với thư mục của bạn

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "index"; // Nó sẽ tìm file index.html trong thư mục resources/templates
    }
}

