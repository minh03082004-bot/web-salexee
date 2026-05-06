package com.salexee.sale_xee.controller;

 // Thay bằng package thực tế của bạn (nhìn ở đầu file CarController)

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Đường dẫn này giúp trình duyệt hiểu rằng: 
        // Khi gọi /images/abc.jpg thì hãy tìm trong thư mục vật lý src/main/resources/static/images/
        registry.addResourceHandler("/images/**")
               .addResourceLocations("file:src/main/resources/static/images/")
                .addResourceLocations("classpath:/static/images/")
                .setCacheControl(org.springframework.http.CacheControl.noCache().mustRevalidate());
    }
}
// package com.salexee.sale_xee.controller;

// import org.springframework.context.annotation.Configuration;
// import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
// import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// @Configuration
// public class WebConfig implements WebMvcConfigurer {

//     @Override
//     public void addResourceHandlers(ResourceHandlerRegistry registry) {

//         registry.addResourceHandler("/images/**")
//                 .addResourceLocations("classpath:/static/images/");
//     }
