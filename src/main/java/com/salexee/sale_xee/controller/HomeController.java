package com.salexee.sale_xee.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
         return "redirect:/cars"; 
//         if(role.equals("ADMIN")){
//     return "redirect:/admin/cars";
// }

// return "redirect:/cars";
    }
   
}

