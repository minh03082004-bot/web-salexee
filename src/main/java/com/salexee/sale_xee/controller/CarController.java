
// package com.salexee.sale_xee.controller;

// //import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Controller;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PostMapping;

// //import com.salexee.sale_xee.CarRepository;

// @Controller
// public class CarController {
//    //@Autowired
// //private CarRepository carRepository;

//     // 1. Trang danh sách xe
//     // @GetMapping("/cars")
//     // public String listCars() {
//     //     return "cars";
//     // }
//     // Khi gõ localhost:8080/cars -> Hiện trang cho khách xem
//     @GetMapping("/cars")
//     public String showUserPage() {
//         return "cars"; 
//      }
//     // Khi gõ localhost:8080/admin/cars -> Hiện trang cho Admin quản lý
//     @GetMapping("/admin/cars")
//     public String showAdminPage() {
//         return "admin-cars";
//     }

//     // // 2. Trang chi tiết xe
//     // @GetMapping("/car-detail")
//     // public String carDetail() {
//     //     return "car-detail";
//     // }
//     // 1. Trang cho User (Cập nhật để truyền danh sách xe)
// // @GetMapping("/cars")
// // public String showUserPage1(Model model) {
// //     model.addAttribute("listXe", carRepository.findAll());
// //     return "cars";
// // }

// // // 2. Trang cho Admin (Cập nhật để truyền danh sách xe)
// // @GetMapping("/admin/cars")
// // public String showAdminPage(Model model) {
// //     model.addAttribute("listXe", carRepository.findAll());
// //     return "admin-cars";
// // }

// // // 3. Trang chi tiết xe (Tạm thời để xem giao diện)
// // @GetMapping("/car-detail")
// // public String carDetail() {
// //     return "car-detail";
// // }

//     // 3. Trang thêm xe (Admin)
//     // @GetMapping("/admin/add-car")
//     // public String addCarForm() {
//     //     return "add-car";
//     // }
//     // 1. Hiển thị trang Form để Admin nhập
//     //   @GetMapping("/admin/add-car")
//     //   public String showAddForm() {
//     //   return "add-car";
//     // }
//     // 2. Nhận dữ liệu từ Form gửi lên (Khi bấm nút Lưu)
//     @PostMapping("/admin/add-car")
//      public String saveCar(String name, Long price, String image, String description) {
//     // Tạm thời in ra màn hình console để kiểm tra
//     System.out.println("Đã nhận xe mới: " + name + " - Giá: " + price);
    
//     // Sau khi lưu xong, quay trở lại trang danh sách admin
//     return "redirect:/admin/cars";
// }
// // @PostMapping("/admin/add-car")
// // public String saveCar(@ModelAttribute Car car) {
// //     carRepository.save(car); // LƯU THẬT VÀO DATABASE
// //     return "redirect:/cars";
// // }
  
//     // Xử lý khi nhấn nút Lưu xe
//     @PostMapping("/admin/add-car")
//     public String saveCar() {
//         // Logic lưu xe vào database sẽ viết ở đây
//         return "redirect:/cars"; // Lưu xong quay về trang danh sách
//     }
//     // @GetMapping("/cars")
//     // public String showUserPage(Model model) {
//     // // carRepository.findAll() sẽ lấy toàn bộ xe đang có trong DB
//     // model.addAttribute("listXe", carRepository.findAll()); 
//     // return "cars";
// //}

// }
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

import com.salexee.sale_xee.Car;
import com.salexee.sale_xee.CarRepository;
import com.salexee.sale_xee.User;
import com.salexee.sale_xee.UserRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class CarController {

    @Autowired
    private CarRepository carRepository;
    @Autowired
private UserRepository userRepository;
    // --- 1. XEM DANH SÁCH (Cả khách và Admin) ---
    // @GetMapping("/cars")
    // public String listCars(Model model) {
    //     model.addAttribute("listXe", carRepository.findAll());
    //     return "cars";
    // }
    @GetMapping("/cars")
public String userCars(Model model,
        // @RequestParam(defaultValue = "0") int page) {
         @RequestParam(defaultValue = "0") int page,
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false) String brand,
        @RequestParam(required = false) Long minPrice,
        @RequestParam(required = false) Long maxPrice
    ) {
         if (keyword != null && keyword.trim().isEmpty()) keyword = null;
if (brand != null && brand.trim().isEmpty()) brand = null;
    Pageable pageable = PageRequest.of(page, 9);

    //Page<Car> carPage = carRepository.findAll(pageable);
     Page<Car> carPage = carRepository.searchCars(
            keyword, brand, minPrice, maxPrice, pageable
    );

    model.addAttribute("listXe", carPage.getContent());
    model.addAttribute("currentPage", page);
    model.addAttribute("totalPages", carPage.getTotalPages());
     // giữ lại giá trị search
    model.addAttribute("keyword", keyword);
    model.addAttribute("brand", brand);
    model.addAttribute("minPrice", minPrice);
    model.addAttribute("maxPrice", maxPrice);
   

    return "cars";
}

    // @GetMapping("/admin/cars")
    // public String adminListCars(Model model) {
    //     model.addAttribute("listXe", carRepository.findAll());
    //     return "admin-cars";
    // }
//     @GetMapping("/admin/cars")
// public String adminListCars(Model model,
//         @RequestParam(defaultValue = "0") int page) {

//     Pageable pageable = PageRequest.of(page, 9);

//     Page<Car> carPage = carRepository.findAll(pageable);

//     model.addAttribute("listXe", carPage.getContent());
//     model.addAttribute("currentPage", page);
//     model.addAttribute("totalPages", carPage.getTotalPages());

//     return "admin-cars";
// }
// @GetMapping("/admin/cars")
// public String listCars(
//         Model model,
//         @RequestParam(defaultValue = "0") int page,
//         @RequestParam(required = false) String keyword,
//         @RequestParam(required = false) String brand,
//         @RequestParam(required = false) Long minPrice,
//         @RequestParam(required = false) Long maxPrice
// ) {

//     Pageable pageable = PageRequest.of(page, 9);

//     Page<Car> carPage = carRepository.searchCars(
//             keyword, brand, minPrice, maxPrice, pageable
//     );

//     model.addAttribute("listXe", carPage.getContent());
//     model.addAttribute("currentPage", page);
//     model.addAttribute("totalPages", carPage.getTotalPages());

//     // giữ lại giá trị search
//     model.addAttribute("keyword", keyword);
//     model.addAttribute("brand", brand);
//     model.addAttribute("minPrice", minPrice);
//     model.addAttribute("maxPrice", maxPrice);

//     return "admin-cars";
// }
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

    return "admin-cars";
}

    @GetMapping("/admin/debug-cars")
    public String debugListCars(Model model) {
        model.addAttribute("cars", carRepository.findAll());
        return "debug-cars";
    }

    // 2. THÊM XE MỚI ---
    // @GetMapping("/admin/add-car")
    // public String showAddForm(Model model) {
    //     model.addAttribute("car", new Car()); // Tạo đối tượng trống để form điền vào
    //     return "add-car";
    // }

    // @PostMapping("/admin/add-car")
    // public String saveCar(@ModelAttribute("car") Car car) {
    //     carRepository.save(car); // Lưu vào DB
    //     return "redirect:/cars"; // Lưu xong nhảy thẳng về trang danh sách
    // }
     // 1. Mở trang web có form để điền thông tin xe mới
@GetMapping("/admin/add")
public String showAddForm(Model model) {
    model.addAttribute("car", new Car()); // Gửi một đối tượng xe trống sang HTML
    return "add-car"; // Tên file HTML
}

// 2. Nhận dữ liệu từ form gửi lên và lưu vào DB
// @PostMapping("/admin/save")
// public String saveCar(@ModelAttribute("car") Car car) {
//     carRepository.save(car); // Vì car này chưa có ID, Spring sẽ tự hiểu là INSERT mới
//     return "redirect:/admin/cars"; // Lưu xong quay về trang danh sách
// }
// @PostMapping("/admin/save") // hoặc /admin/update/{id}
// public String saveCar(@ModelAttribute("car") Car car, 
//                       @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {
//     if (imageFile != null && !imageFile.isEmpty()) {
//         try {
//             String originalFileName = imageFile.getOriginalFilename();
//             String fileName = System.currentTimeMillis() + "_" + originalFileName;

//             Path srcPath = Paths.get("src/main/resources/static/images/" + fileName);
//             Path targetPath = Paths.get("target/classes/static/images/" + fileName);

//             Files.createDirectories(srcPath.getParent());
//             Files.createDirectories(targetPath.getParent());

//             Files.write(srcPath, imageFile.getBytes());
//             Files.write(targetPath, imageFile.getBytes());

//             car.setImage("/images/" + fileName);
//             System.out.println("[UPLOAD] File saved: " + fileName + " -> " + car.getImage());
//         } catch (IOException e) {
//             e.printStackTrace();
//             if (car.getImage() == null || car.getImage().isBlank()) {
//                 car.setImage("/images/default-car.jpg");
//             }
//         }
//     } else if (car.getImage() != null && !car.getImage().isBlank()) {
//         // giữ lại giá trị URL nhập tay
//         System.out.println("[UPLOAD] Dùng link ảnh URL: " + car.getImage());
//     } else {
//         // Nếu không có file và không có URL thì đặt mặc định
//         car.setImage("/images/default-car.jpg");
//         System.out.println("[UPLOAD] Không có ảnh, dùng ảnh mặc định");
//     }
//     carRepository.save(car);
//     return "redirect:/admin/cars";
// }
// @PostMapping("/admin/save")
// public String saveCar(@ModelAttribute Car car,
//                       @RequestParam("imageFile") MultipartFile file) {

//     try {
//         if (!file.isEmpty()) {

//             String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
//             String uploadDir = "src/main/resources/static/images/";

//             File uploadPath = new File(uploadDir);
//             if (!uploadPath.exists()) {
//                 uploadPath.mkdirs();
//             }

//             file.transferTo(new File(uploadDir + fileName));

//             car.setImage("/images/" + fileName);
//         } else if (car.getImage() == null || car.getImage().isBlank()) {
//             car.setImage("https://via.placeholder.com/500x300?text=No+Image");
//         }

//         carRepository.save(car);

//     } catch (Exception e) {
//         e.printStackTrace();
//         if (car.getImage() == null || car.getImage().isBlank()) {
//             car.setImage("https://via.placeholder.com/500x300?text=No+Image");
//             carRepository.save(car);
//         }
//     }

//     return "redirect:/admin/cars";
// } 

// @PostMapping("/admin/save")
// public String saveCar(@ModelAttribute Car car,
//                       @RequestParam("imageFile") MultipartFile imageFile, 
//                       RedirectAttributes redirectAttributes) 
//                       throws IOException {

//     if (!imageFile.isEmpty()) {

//         // tạo tên file tránh trùng
//         String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();

//         // đường dẫn lưu file
//         Path path = Paths.get("src/main/resources/static/images/" + fileName);

//         // tạo thư mục nếu chưa có
//         Files.createDirectories(path.getParent());

//         // lưu file
//         Files.write(path, imageFile.getBytes());

//         // lưu đường dẫn vào database
//         car.setImage("/images/" + fileName);

//     } else {
//         // nếu không chọn ảnh
//         car.setImage("/images/default-car.jpg");
//     }

//     carRepository.save(car);
//     // gửi thông báo
//     redirectAttributes.addFlashAttribute("success", "Thêm xe thành công!");

//     return "redirect:/admin/cars";
// }
// @PostMapping("/admin/save")
// public String saveCar(
//         @ModelAttribute Car car,
//         @RequestParam("imageFile") MultipartFile imageFile,
//         @RequestParam("subImages") MultipartFile[] subImages
// ) throws IOException {

//     String uploadDir = "src/main/resources/static/images/";

//     // ===== ẢNH CHÍNH =====
//     if (!imageFile.isEmpty()) {
//         String fileName = imageFile.getOriginalFilename();
//         imageFile.transferTo(new File(uploadDir + fileName));
//         car.setImage(fileName);
//     }

//     // ===== ẢNH NHỎ =====
//     List<String> imageList = new ArrayList<>();

//     for (MultipartFile file : subImages) {
//         if (!file.isEmpty()) {
//             String fileName = file.getOriginalFilename();
//             file.transferTo(new File(uploadDir + fileName));
//             imageList.add(fileName);
//         }
//     }

//     // nối thành chuỗi để lưu DB
//     if (!imageList.isEmpty()) {
//         car.setImages(String.join(",", imageList));
//     }

//     // lưu DB
//     carRepository.save(car);

//     return "redirect:/admin/cars";
// }
@PostMapping("/admin/save")
public String saveCar(
        @ModelAttribute Car car,
        @RequestParam("imageFile") MultipartFile imageFile,
        @RequestParam(value = "subImages", required = false) MultipartFile[] subImages,
        RedirectAttributes redirectAttributes
) throws IOException {

    String uploadDir = "src/main/resources/static/images/";

    // ===== ẢNH CHÍNH =====
    if (!imageFile.isEmpty()) {

        String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();

        Path path = Paths.get(uploadDir + fileName);
        Files.createDirectories(path.getParent());
        Files.write(path, imageFile.getBytes());

        car.setImage(fileName); // ⚠ KHÔNG thêm /images/
    }

    // ===== ẢNH NHỎ =====
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
    // --- 3. XEM CHI TIẾT ---
    // @GetMapping("/car-detail/{id}")
    // public String viewDetail(@PathVariable Long id, Model model) {
    //     // Car car = carRepository.findById(id).orElse(null);
    //     Car car = carRepository.findById(id)
    //       .orElseThrow(() -> new RuntimeException("Không tìm thấy xe!"));
    //     model.addAttribute("car", car);
    //     return "car-detail";
    // }
 

@GetMapping("/car-detail/{id}")
public String carDetail(@PathVariable Long id, Model model) {

    Car car = carRepository.findById(id).orElse(null);

    // List<String> imageList = new ArrayList<>();

    // if (car.getImages() != null && !car.getImages().isEmpty()) {
    //     imageList = Arrays.asList(car.getImages().split(","));
    // }

    // model.addAttribute("car", car);
    // model.addAttribute("imageList", imageList);

    // return "car-detail";



    List<String> imageList = new ArrayList<>();

// thêm ảnh chính (ngoại thất) trước
// if (car.getImage() != null && !car.getImage().isEmpty()) {
//     imageList.add(car.getImage());
// }

// thêm ảnh phụ (nội thất)
// if (car.getImages() != null && !car.getImages().isEmpty()) {
//     imageList.addAll(Arrays.asList(car.getImages().split(",")));
// }
// CHỈ lấy ảnh phụ (nội thất)
if (car.getImages() != null && !car.getImages().isEmpty()) {
    imageList = Arrays.asList(car.getImages().split(","));
}

model.addAttribute("car", car);
     model.addAttribute("imageList", imageList);

    return "car-detail";
}

    // --- 4. XÓA XE ---
    // @GetMapping("/admin/delete/{id}")
    // public String deleteCar(@PathVariable Long id) {
    //     carRepository.deleteById(id);
    //     return "redirect:/admin/cars"; // Xóa xong quay lại trang admin
    // }
    @GetMapping("/admin/delete/{id}")
public String deleteCar(@PathVariable Long id, RedirectAttributes redirectAttributes) {

    carRepository.deleteById(id);

    redirectAttributes.addFlashAttribute("success", "Xóa xe thành công!");

    return "redirect:/admin/cars";
}
//     @GetMapping("/admin/delete/{id}")
// public String deleteCar(@PathVariable("id") Long id) {
//     try {
//         // Tìm xe để lấy thông tin ảnh trước khi xóa dữ liệu (nếu muốn xóa cả file ảnh vật lý)
//         Car car = carRepository.findById(id)
//                 .orElseThrow(() -> new RuntimeException("Không tìm thấy xe để xóa"));

//         // (Tùy chọn) Xóa file ảnh vật lý trong thư mục images để tránh đầy bộ nhớ
//         if (car.getImage() != null && car.getImage().startsWith("/images/")) {
//             Path path = Paths.get("src/main/resources/static" + car.getImage());
//             Files.deleteIfExists(path);
//         }

//         // Xóa dòng dữ liệu trong database
//         carRepository.deleteById(id);

//     } catch (Exception e) {
//         e.printStackTrace();
//     }
//     // Sau khi xóa xong, quay về trang danh sách quản lý
//     return "redirect:/admin/cars";
// }

    // --- 5. SỬA XE ---
    @GetMapping("/admin/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Car car = carRepository.findById(id).orElse(null);
        model.addAttribute("car", car);
        return "edit-car"; // Dùng chung form add-car nhưng có dữ liệu cũ
    }
    // Xử lý lưu dữ liệu sau khi sửa
    // @PostMapping("/admin/update/{id}")
    // public String updateCar(@PathVariable("id") Long id, @ModelAttribute("car") Car car) {
    // car.setId(id); // Đảm bảo ID đúng để Spring Boot thực hiện Update thay vì Insert mới
    // carRepository.save(car); 
    // return "redirect:/admin/cars"; // Sửa xong quay về trang quản lý của Admin
    // }
    // @PostMapping("/admin/update/{id}")
    // public String updateCar(@PathVariable("id") Long id,
    //                         @ModelAttribute("car") Car car,
    //                         @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {

    //     Car current = carRepository.findById(id)
    //             .orElseThrow(() -> new RuntimeException("Không tìm thấy xe!"));

    //     if (imageFile != null && !imageFile.isEmpty()) {
    //         try {
    //             String originalFileName = imageFile.getOriginalFilename();
    //             String fileName = System.currentTimeMillis() + "_" + originalFileName;
    //             Path path = Paths.get("src/main/resources/static/images/" + fileName);
    //             Files.createDirectories(path.getParent());
    //             Files.write(path, imageFile.getBytes());
    //             car.setImage("/images/" + fileName);
    //         } catch (IOException e) {
    //             e.printStackTrace();
    //             car.setImage(current.getImage());
    //         }
    //     } else if (car.getImage() != null && !car.getImage().isBlank()) {
    //         // phù hợp khi dùng link URL trực tiếp trong form edit
    //         System.out.println("[UPDATE] Giữ URL ảnh nhập tay: " + car.getImage());
    //     } else {
    //         car.setImage(current.getImage() != null && !current.getImage().isBlank() ? current.getImage() : "/images/default-car.jpg");
    //         System.out.println("[UPDATE] Giữ ảnh cũ hoặc ảnh mặc định: " + car.getImage());
    //     }

    //     car.setId(id);
    //     carRepository.save(car);
    //     return "redirect:/admin/cars";
    //}




//       @PostMapping("/admin/update/{id}")
// public String updateCar(@PathVariable("id") Long id,
//                         @ModelAttribute("car") Car car,
//                         @RequestParam(value = "imageFile", required = false) MultipartFile file) {

//     try {

//         // Lấy dữ liệu xe cũ từ database
//         Car oldCar = carRepository.findById(id)
//                 .orElseThrow(() -> new RuntimeException("Không tìm thấy xe"));

//         // Nếu có upload ảnh mới
//         if (file != null && !file.isEmpty()) {

//             String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
//             String uploadDir = "src/main/resources/static/images/";

//             File folder = new File(uploadDir);
//             if (!folder.exists()) {
//                 folder.mkdirs();
//             }

//             file.transferTo(new File(uploadDir + fileName));

//             car.setImage("/images/" + fileName);

//         } else {
//             // Nếu không upload ảnh mới → giữ ảnh cũ
//             String oldImage = oldCar.getImage();
//             if (oldImage != null && !oldImage.isBlank()) {
//                 car.setImage(oldImage);
//             } else {
//                 car.setImage("https://via.placeholder.com/500x300?text=No+Image");
//             }
//         }

//         // Giữ đúng ID
//         car.setId(id);

//         // Lưu vào database
//         carRepository.save(car);

//     } catch (Exception e) {
//         e.printStackTrace();
//     }

//     return "redirect:/admin/cars";
// }
// @PostMapping("/admin/update/{id}")
// public String updateCar(@PathVariable("id") Long id, 
//                         @ModelAttribute("car") Car car, 
//                         @RequestParam(value = "imageFile", required = false) MultipartFile file) {
//     try {
//         // 1. Lấy dữ liệu xe cũ từ database để biết đường dẫn ảnh hiện tại
//         Car oldCar = carRepository.findById(id)
//                 .orElseThrow(() -> new RuntimeException("Không tìm thấy xe"));

        // 2. Xử lý ảnh
        // if (file != null && !file.isEmpty()) {
        //     // Nếu có upload ảnh mới -> Lưu file và cập nhật đường dẫn mới
        //     String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        //     String uploadDir = "src/main/resources/static/images/";
        //     file.transferTo(new File(uploadDir + fileName));
        //     car.setImage("/images/" + fileName);
//         if (imageFile != null && !imageFile.isEmpty()) {
//     // Lưu file vào thư mục images
//     String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
//     imageFile.transferTo(new File("src/main/resources/static/images/" + fileName));
//     car.setImage("/images/" + fileName); // Đây là dòng gán ảnh mới
// } else {
//     car.setImage(oldCar.getImage()); // Nếu không chọn ảnh mới thì lấy ảnh cũ từ Database
// }
        // } else {
        //     // Nếu KHÔNG upload ảnh mới -> Lấy lại đường dẫn ảnh cũ từ database
        //     car.setImage(oldCar.getImage());
        // }
        // if(file != null && !file.isEmpty()) {
        //     String fileName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
        //     String uploadDir = "src/main/resources/static/images/";
        //     File folder = new File(uploadDir);
        //     if (!folder.exists()) {
        //         folder.mkdirs();
        //     }
        //     file.transferTo(new File(uploadDir + fileName));
        //     car.setImage("/images/" + fileName);
        // } else if (car.getImage() != null && !car.getImage().isBlank()) {
        //     // Nếu có nhập link URL mới trong form edit -> dùng link đó
        //     System.out.println("[UPDATE] Dùng link URL mới nhập tay: " + car.getImage());
        // }
        // } else {
        //     // Nếu không có file mới và không có URL mới -> giữ lại ảnh cũ
        //     car.setImage(oldCar.getImage() != null && !oldCar.getImage().isBlank() ? oldCar.getImage() : "https://via.placeholder.com/500x300?text=No+Image");
        //     System.out.println("[UPDATE] Giữ lại ảnh cũ hoặc đặt mặc định: " + car.getImage());
        // }

        // 3. Đảm bảo ID chính xác và lưu
//         car.setId(id);
//         carRepository.save(car);

//     } catch (IOException e) {
//         e.printStackTrace();
//     }
//     return "redirect:/admin/cars";
// }
// @PostMapping("/admin/update/{id}")
// public String updateCar(@PathVariable Long id,
//                         @ModelAttribute Car car,
//                         @RequestParam("imageFile") MultipartFile imageFile,
//                         RedirectAttributes redirectAttributes) throws IOException {

//     Car oldCar = carRepository.findById(id).orElseThrow();

//     if (!imageFile.isEmpty()) {
//         String fileName = imageFile.getOriginalFilename();
//         Path path = Paths.get("src/main/resources/static/images/" + fileName);
//         Files.write(path, imageFile.getBytes());

//         car.setImage("/images/" + fileName);
//     } else {
//         car.setImage(oldCar.getImage());
//     }

//     car.setId(id);
//     carRepository.save(car);
//     redirectAttributes.addFlashAttribute("success", "Cập nhật thông tin xe thành công!");

//     return "redirect:/admin/cars";
// }

@PostMapping("/admin/update/{id}")
public String updateCar(
        @ModelAttribute Car car,
        @RequestParam("imageFile") MultipartFile imageFile,
        @RequestParam(value = "subImages", required = false) MultipartFile[] subImages,
           RedirectAttributes redirectAttributes
) throws IOException {

    String uploadDir = "src/main/resources/static/images/";

    // ===== LẤY XE CŨ =====
    Car oldCar = carRepository.findById(car.getId()).orElse(null);

    // ===== ẢNH CHÍNH =====
    if (!imageFile.isEmpty()) {

        String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();

        Path path = Paths.get(uploadDir + fileName);
        Files.createDirectories(path.getParent());
        Files.write(path, imageFile.getBytes());

        car.setImage(fileName);

    } else {
        // giữ ảnh cũ
        car.setImage(oldCar.getImage());
    }

    // ===== ẢNH NHỎ =====
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
        // giữ ảnh nhỏ cũ
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
}