package com.salexee.sale_xee.controller;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.salexee.sale_xee.Brand;
import com.salexee.sale_xee.BrandRepository;
import com.salexee.sale_xee.Car;
import com.salexee.sale_xee.CarRepository;
import com.salexee.sale_xee.CartRepository;
import com.salexee.sale_xee.OrderItem;
import com.salexee.sale_xee.OrderRepository;
import com.salexee.sale_xee.OrderView;
import com.salexee.sale_xee.User;
import com.salexee.sale_xee.UserRepository;

import jakarta.servlet.http.HttpSession;
@Controller
public class CarController {

    @Autowired
    private CarRepository carRepository;
    @Autowired
private UserRepository userRepository;
@Autowired
private CartRepository cartRepository;
@Autowired
private OrderRepository orderRepository;
@Autowired
private BrandRepository brandRepository;
    
@GetMapping("/cars")
public String userCars(Model model,HttpSession session,
         @RequestParam(defaultValue = "0") int page,
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false) String brand,
        @RequestParam(required = false) Long minPrice,
        @RequestParam(required = false) Long maxPrice
    ) {
         if (keyword != null && keyword.trim().isEmpty()) keyword = null;
if (brand != null && brand.trim().isEmpty()) brand = null;
    Pageable pageable = PageRequest.of(page, 9);
     Page<Car> carPage = carRepository.searchCars(
            keyword, brand, minPrice, maxPrice, pageable
    );
       User user = (User) session.getAttribute("user");

    if (user != null) {

        long cartCount =
                cartRepository.countByUsername(user.getUsername());

        model.addAttribute("cartCount", cartCount);
    }

    model.addAttribute("listXe", carPage.getContent());
    model.addAttribute("currentPage", page);
    model.addAttribute("totalPages", carPage.getTotalPages());
    model.addAttribute("keyword", keyword);
    model.addAttribute("brand", brand);
    model.addAttribute("minPrice", minPrice);
    model.addAttribute("maxPrice", maxPrice);
      model.addAttribute(
            "brands",
            brandRepository.findAll()
    );

    return "cars";
}
@GetMapping("/admin/cars")
public String adminCars(Model model,
                        HttpSession session,
                        
                     @RequestParam(defaultValue = "0") int page,
         @RequestParam(required = false) String keyword,
         @RequestParam(required = false) String brand,
         @RequestParam(required = false) Long minPrice,
         @RequestParam(required = false) Long maxPrice) {

    User user = (User) session.getAttribute("user");

    if (user == null) {
        return "redirect:/login";
    }

    if (!"ADMIN".equalsIgnoreCase(user.getRole())) {
         return "redirect:/cars";
        
    }

    Pageable pageable = PageRequest.of(page, 9);

    Page<Car> carPage = carRepository.searchCars(
             keyword, brand, minPrice, maxPrice, pageable
    );
    model.addAttribute("listXe", carPage.getContent());
    model.addAttribute("currentPage", page);
    model.addAttribute("totalPages", carPage.getTotalPages());

    model.addAttribute("keyword", keyword);
    model.addAttribute("brand", brand);
    model.addAttribute("minPrice", minPrice);
    model.addAttribute("maxPrice", maxPrice);
     model.addAttribute(
            "brands",
            brandRepository.findAll()
    );

    return "admin-cars";
}

    @GetMapping("/admin/debug-cars")
    public String debugListCars(Model model) {
        model.addAttribute("cars", carRepository.findAll());
        return "debug-cars";
    }

// @GetMapping("/admin/add")
// public String showAddForm(Model model) {
//     model.addAttribute("car", new Car()); 
//     return "add-car"; 
// }
@GetMapping("/admin/add")
public String addCarForm(Model model) {

    model.addAttribute("car", new Car());

    model.addAttribute(
            "brands",
            brandRepository.findAll()
    );

    return "add-car";
}

@PostMapping("/admin/save")
public String saveCar(
        @ModelAttribute Car car,
        @RequestParam("imageFile") MultipartFile imageFile,
        @RequestParam(value = "subImages", required = false) MultipartFile[] subImages,
        RedirectAttributes redirectAttributes
) throws IOException {
    String uploadDir = "src/main/resources/static/images/";
    if (!imageFile.isEmpty()) {
        String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
        Path path = Paths.get(uploadDir + fileName);
        Files.createDirectories(path.getParent());
        Files.write(path, imageFile.getBytes());

        car.setImage(fileName); 
    }
    List<String> imageList = new ArrayList<>();
    if (subImages != null) {
        for (MultipartFile file : subImages) {
            if (!file.isEmpty()) {

                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

                Path path = Paths.get(uploadDir + fileName);
                Files.createDirectories(path.getParent());
                Files.write(path, file.getBytes());

                imageList.add(fileName);
            }
        }
    }

    if (!imageList.isEmpty()) {
        car.setImages(String.join(",", imageList));
    }

    carRepository.save(car);
    redirectAttributes.addFlashAttribute("success", "Thêm xe thành công!");

    return "redirect:/admin/cars";
}

@GetMapping("/car-detail/{id}")
public String carDetail(@PathVariable Long id, Model model) {

    Car car = carRepository.findById(id).orElse(null);
    List<String> imageList = new ArrayList<>();
if (car.getImages() != null && !car.getImages().isEmpty()) {
    imageList = Arrays.asList(car.getImages().split(","));
}
model.addAttribute("car", car);
     model.addAttribute("imageList", imageList);

    return "car-detail";
}
    @GetMapping("/admin/delete/{id}")
public String deleteCar(@PathVariable Long id, RedirectAttributes redirectAttributes) {

    carRepository.deleteById(id);

    redirectAttributes.addFlashAttribute("success", "Xóa xe thành công!");

    return "redirect:/admin/cars";
}

    @GetMapping("/admin/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Car car = carRepository.findById(id).orElse(null);
        model.addAttribute("car", car);
        return "edit-car"; 
    }

@PostMapping("/admin/update/{id}")
public String updateCar(
        @ModelAttribute Car car,
        @RequestParam("imageFile") MultipartFile imageFile,
        @RequestParam(value = "subImages", required = false) MultipartFile[] subImages,
           RedirectAttributes redirectAttributes
) throws IOException {

    String uploadDir = "src/main/resources/static/images/";
    Car oldCar = carRepository.findById(car.getId()).orElse(null);
    if (!imageFile.isEmpty()) {

        String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();

        Path path = Paths.get(uploadDir + fileName);
        Files.createDirectories(path.getParent());
        Files.write(path, imageFile.getBytes());

        car.setImage(fileName);

    } else {
        car.setImage(oldCar.getImage());
    }
    List<String> imageList = new ArrayList<>();

    if (subImages != null && subImages.length > 0 && !subImages[0].isEmpty()) {

        for (MultipartFile file : subImages) {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

            Path path = Paths.get(uploadDir + fileName);
            Files.createDirectories(path.getParent());
            Files.write(path, file.getBytes());

            imageList.add(fileName);
        }

        car.setImages(String.join(",", imageList));

    } else {
        car.setImages(oldCar.getImages());
    }

    carRepository.save(car);
   redirectAttributes.addFlashAttribute("success", "Cập nhật thông tin xe thành công!");
    return "redirect:/admin/cars";
}
@GetMapping("/admin/dashboard")
public String dashboard(Model model) {
  List<Object[]> stats = carRepository.countCarsByBrand();
model.addAttribute("brandStats", stats);
    long totalCars = carRepository.count();
    // long totalUsers = userRepository.count();
    long totalUsers = userRepository.countByRole("USER");
    long totalAdmins = userRepository.countByRole("ADMIN");
model.addAttribute("totalUsers", totalUsers);
model.addAttribute("totalAdmins", totalAdmins);
    model.addAttribute("totalCars", totalCars);
    // model.addAttribute("totalUsers", totalUsers);

    return "admin-dashboard";
}
@GetMapping("/admin/orders")
public String adminOrders(Model model) {

    List<OrderItem> orders =
            orderRepository.findAllByOrderByOrderDateDesc();

    List<OrderView> orderViews = new ArrayList<>();

    for (OrderItem item : orders) {

        Car car = carRepository
                .findById(item.getCarId())
                .orElse(null);

        if (car != null) {

            orderViews.add(

                    new OrderView(
                            item.getUsername(),
                            car,
                            item.getOrderDate(),
                            item.getPhone()
                    )
            );
        }
    }

    model.addAttribute("orders", orderViews);

    return "admin-orders";
}
@GetMapping("/admin/users")
public String users(Model model) {

    model.addAttribute(
            "users",
            userRepository.findAll()
    );

    return "admin-users";
}
@GetMapping("/admin/lock/{id}")
public String lockUser(@PathVariable Long id) {

    User user =
            userRepository.findById(id)
            .orElse(null);

    if(user != null) {

        user.setEnabled(false);

        userRepository.save(user);
    }

    return "redirect:/admin/users";
}
@GetMapping("/admin/unlock/{id}")
public String unlockUser(@PathVariable Long id) {

    User user =
            userRepository.findById(id)
            .orElse(null);

    if(user != null) {

        user.setEnabled(true);

        userRepository.save(user);
    }

    return "redirect:/admin/users";
}
@GetMapping("/admin/brands")
public String brands(Model model) {

    model.addAttribute(
            "brands",
            brandRepository.findAll()
    );

    return "admin-brands";
}
@GetMapping("/admin/brands/add")
public String addBrandForm() {

    return "add-brand";
}
@PostMapping("/admin/brands/save")
public String saveBrand(
        @RequestParam String name,
        @RequestParam("logoFile")
        MultipartFile logoFile
) throws IOException {

    String fileName =
            System.currentTimeMillis()
            + "_"
            + logoFile.getOriginalFilename();

    Path path = Paths.get(
            "src/main/resources/static/images/"
            + fileName
    );

    Files.createDirectories(path.getParent());

    Files.write(path, logoFile.getBytes());

    Brand brand = new Brand();

    brand.setName(name);

    brand.setLogo(fileName);

    brandRepository.save(brand);

    return "redirect:/admin/brands";
}
}