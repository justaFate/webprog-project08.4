package webprog.configs;

import java.util.Date;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import webprog.entity.Category;
import webprog.entity.Product;
import webprog.repository.CategoryRepository;
import webprog.repository.ProductRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(CategoryRepository categoryRepository, ProductRepository productRepository) {
        return args -> {
            if (categoryRepository.count() == 0) {
                Category cat1 = new Category(null, "Quần Áo Nam", null, null);
                Category cat2 = new Category(null, "Quần Áo Nữ", null, null);
                Category cat3 = new Category(null, "Quần Áo Trẻ em", null, null);
                Category cat4 = new Category(null, "Giày Thể Thao", null, null);
                Category cat5 = new Category(null, "Phụ Kiện Thời Trang", null, null);

                categoryRepository.save(cat1);
                categoryRepository.save(cat2);
                categoryRepository.save(cat3);
                categoryRepository.save(cat4);
                categoryRepository.save(cat5);

                Product p1 = new Product(null, "Áo Thun Nam Cotton", 50, 150000.0, null, "Áo thun nam phong cách trẻ trung thoáng mát", 10.0, new Date(), (short) 1, cat1);
                Product p2 = new Product(null, "Áo Sơ Mi Nam Công Sở", 30, 320000.0, null, "Áo sơ mi nam cao cấp chống nhăn", 15.0, new Date(), (short) 1, cat1);
                Product p3 = new Product(null, "Váy Nữ Dáng Xòe", 25, 280000.0, null, "Váy nữ thanh lịch dịu dàng", 5.0, new Date(), (short) 1, cat2);
                Product p4 = new Product(null, "Áo Khoác Nữ Cardigan", 15, 450000.0, null, "Áo khoác phong cách hiện đại", 20.0, new Date(), (short) 1, cat2);
                Product p5 = new Product(null, "Bộ Đồ Trẻ Em Mùa Hè", 40, 120000.0, null, "Chất liệu cotton mềm mại cho bé", 0.0, new Date(), (short) 1, cat3);
                Product p6 = new Product(null, "Giày Sneaker Trắng Năng Động", 20, 550000.0, null, "Giày thể thao đế êm cao cấp", 10.0, new Date(), (short) 1, cat4);
                Product p7 = new Product(null, "Túi Đeo Chéo Canvas Unisex", 60, 95000.0, null, "Túi canvas phong cách Hàn Quốc", 0.0, new Date(), (short) 1, cat5);
                Product p8 = new Product(null, "Mũ Lưỡi Trai Classic", 80, 75000.0, null, "Mũ vải kaki thoáng mát", 0.0, new Date(), (short) 1, cat5);

                productRepository.save(p1);
                productRepository.save(p2);
                productRepository.save(p3);
                productRepository.save(p4);
                productRepository.save(p5);
                productRepository.save(p6);
                productRepository.save(p7);
                productRepository.save(p8);

                System.out.println("Sample data initialized successfully!");
            }
        };
    }
}

