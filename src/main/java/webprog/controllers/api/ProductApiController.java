package webprog.controllers.api;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import webprog.entity.Category;
import webprog.entity.Product;
import webprog.model.Response;
import webprog.services.ICategoryService;
import webprog.services.IProductService;
import webprog.services.IStorageService;

@RestController
@RequestMapping(path = "/api/product")
@Tag(name = "product-api-controller", description = "Product Management REST API")
public class ProductApiController {

    @Autowired
    private IProductService productService;

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private IStorageService storageService;

    @Operation(summary = "Lấy tất cả sản phẩm")
    @GetMapping
    public ResponseEntity<?> getAllProduct() {
        return new ResponseEntity<Response>(
                new Response(true, "Thành công", productService.findAll()),
                HttpStatus.OK);
    }

    @Operation(summary = "Lấy thông tin sản phẩm theo ID")
    @PostMapping(path = "/getProduct")
    public ResponseEntity<?> getProduct(@Validated @RequestParam("id") Long id) {
        Optional<Product> product = productService.findById(id);
        if (product.isPresent()) {
            return new ResponseEntity<Response>(
                    new Response(true, "Thành công", product.get()),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<Response>(
                    new Response(false, "Không tìm thấy sản phẩm", null),
                    HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Thêm sản phẩm mới")
    @PostMapping(path = "/addProduct", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE })
    public ResponseEntity<?> addProduct(
            @Validated @RequestParam("productName") String productName,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            @Validated @RequestParam("unitPrice") Double unitPrice,
            @Validated @RequestParam("discount") Double discount,
            @Validated @RequestParam("description") String description,
            @Validated @RequestParam("categoryId") Long categoryId,
            @Validated @RequestParam("quantity") Integer quantity,
            @Validated @RequestParam("status") Short status) {

        Optional<Product> optProduct = productService.findByProductName(productName);
        if (optProduct.isPresent()) {
            return new ResponseEntity<Response>(
                    new Response(false, "Sản phẩm này đã tồn tại trong hệ thống", optProduct.get()),
                    HttpStatus.BAD_REQUEST);
        }

        Product product = new Product();
        product.setProductName(productName);
        product.setUnitPrice(unitPrice);
        product.setDiscount(discount);
        product.setDescription(description);
        product.setQuantity(quantity);
        product.setStatus(status);
        product.setCreateDate(new Date());

        Optional<Category> optCategory = categoryService.findById(categoryId);
        if (optCategory.isPresent()) {
            product.setCategory(optCategory.get());
        } else {
            return new ResponseEntity<Response>(
                    new Response(false, "Danh mục không tồn tại", null),
                    HttpStatus.BAD_REQUEST);
        }

        if (imageFile != null && !imageFile.isEmpty()) {
            UUID uuid = UUID.randomUUID();
            String uuString = uuid.toString();
            product.setImages(storageService.getSorageFilename(imageFile, uuString));
            storageService.store(imageFile, product.getImages());
        }

        Product savedProduct = productService.save(product);
        return new ResponseEntity<Response>(
                new Response(true, "Thành công", savedProduct),
                HttpStatus.OK);
    }

    @Operation(summary = "Cập nhật sản phẩm")
    @RequestMapping(path = "/updateProduct", method = { RequestMethod.PUT, RequestMethod.POST }, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE })
    public ResponseEntity<?> updateProduct(
            @Validated @RequestParam("productId") Long productId,
            @Validated @RequestParam("productName") String productName,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            @Validated @RequestParam("unitPrice") Double unitPrice,
            @Validated @RequestParam("discount") Double discount,
            @Validated @RequestParam("description") String description,
            @Validated @RequestParam("categoryId") Long categoryId,
            @Validated @RequestParam("quantity") Integer quantity,
            @Validated @RequestParam("status") Short status) {

        Optional<Product> optProduct = productService.findById(productId);
        if (optProduct.isEmpty()) {
            return new ResponseEntity<Response>(
                    new Response(false, "Không tìm thấy sản phẩm", null),
                    HttpStatus.BAD_REQUEST);
        }

        Product product = optProduct.get();
        product.setProductName(productName);
        product.setUnitPrice(unitPrice);
        product.setDiscount(discount);
        product.setDescription(description);
        product.setQuantity(quantity);
        product.setStatus(status);

        Optional<Category> optCategory = categoryService.findById(categoryId);
        if (optCategory.isPresent()) {
            product.setCategory(optCategory.get());
        }

        if (imageFile != null && !imageFile.isEmpty()) {
            UUID uuid = UUID.randomUUID();
            String uuString = uuid.toString();
            product.setImages(storageService.getSorageFilename(imageFile, uuString));
            storageService.store(imageFile, product.getImages());
        }

        Product updatedProduct = productService.save(product);
        return new ResponseEntity<Response>(
                new Response(true, "Cập nhật thành công", updatedProduct),
                HttpStatus.OK);
    }

    @Operation(summary = "Xóa sản phẩm")
    @DeleteMapping(path = "/deleteProduct")
    public ResponseEntity<?> deleteProduct(@Validated @RequestParam("productId") Long productId) {
        Optional<Product> optProduct = productService.findById(productId);
        if (optProduct.isEmpty()) {
            return new ResponseEntity<Response>(
                    new Response(false, "Không tìm thấy sản phẩm", null),
                    HttpStatus.BAD_REQUEST);
        } else {
            productService.delete(optProduct.get());
            return new ResponseEntity<Response>(
                    new Response(true, "Xóa thành công", optProduct.get()),
                    HttpStatus.OK);
        }
    }

    @Operation(summary = "Tìm kiếm và phân trang sản phẩm")
    @GetMapping(path = "/paginated")
    public ResponseEntity<?> getPaginatedProducts(
            @RequestParam(name = "name", defaultValue = "") String name,
            @RequestParam(name = "categoryId", required = false) Long categoryId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size,
            @RequestParam(name = "sort", defaultValue = "productId") String sort,
            @RequestParam(name = "dir", defaultValue = "asc") String dir) {

        Sort sortOrder = dir.equalsIgnoreCase("desc") ? Sort.by(sort).descending() : Sort.by(sort).ascending();
        Pageable pageable = PageRequest.of(Math.max(0, page), size, sortOrder);

        Page<Product> result = productService.search(name, categoryId, pageable);
        return new ResponseEntity<Response>(
                new Response(true, "Thành công", result),
                HttpStatus.OK);
    }
}

