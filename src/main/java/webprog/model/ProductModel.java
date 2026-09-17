package webprog.model;

import org.springframework.web.multipart.MultipartFile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductModel {
    private Long productId;
    private String productName;
    private Integer quantity;
    private Double unitPrice;
    private MultipartFile imageFile;
    private String images;
    private String description;
    private Double discount;
    private Short status;
    private Long categoryId;
}

