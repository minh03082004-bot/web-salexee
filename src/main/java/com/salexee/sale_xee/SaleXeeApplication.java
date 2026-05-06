package com.salexee.sale_xee;

 import org.springframework.boot.CommandLineRunner;
 import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class SaleXeeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SaleXeeApplication.class, args);
    } // Đh main
    @Bean
     CommandLineRunner initDatabase(CarRepository repository) {
    return args -> {    
        // Lưu ý: Phải nằm trong dấu ngoặc nhọn này thì xe mới được nạp vào máy
     //   repository.save(new Car("Porsche 911 Carrera", 6900000000L, "https://images.unsplash.com/photo-1503376780353-7e6692767b70?w=500", "2024"));
       // repository.save(new Car("Mercedes-Benz G63", 11000000000L, "https://images.unsplash.com/photo-1520031441872-265e4ff70366?w=500", "2023"));
        //repository.save(new Car("Audi R8 V10", 9500000000L, "https://images.unsplash.com/photo-1614164185128-e4ec99c436d7?w=500", "2024"));
        //repository.save(new Car("Tesla Model S", 3500000000L, "https://images.unsplash.com/photo-1617788138017-80ad40651399?w=500", "2025"));
    };
}

  
} // Dấu ngoặc này phải là dấu cuối cùng của file!