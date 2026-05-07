package com.salexee.sale_xee;
 
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "cars")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Long price;
    private String image;
    private String year;
    private String images;
    private String description;
    @Column(name = "brand")
      private String brand;
    public Car() {
    }
    public Car(String name, Long price, String image,String year,String description,String brand, String images) {
        this.name = name;
        this.price = price;
        this.image = image;
        this. year = year;
        this.images = images;
        this.description=description;
        this.brand=brand;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getBrand() {
    return brand;
    }

    public void setBrand(String brand) {
    this.brand = brand;
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Long getPrice() { return price; }
    public void setPrice(Long price) { this.price = price; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public String getYear() { return year; }
    public void setYear(String year) { this.year = year; }
    public String getImages() {
    return images;
}

public void setImages(String images) {
    this.images = images;
}
}