package webprog.controllers.api;

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
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import webprog.entity.Category;
import webprog.model.Response;
import webprog.services.ICategoryService;
import webprog.services.IStorageService;

@RestController
@RequestMapping(path = "/api/category")
@Tag(name = "category-api-controller", description = "Category Management REST API")
public class CategoryAPIController {

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private IStorageService storageService;

    @Operation(summary = "Lấy tất cả danh mục")
    @GetMapping
    public ResponseEntity<?> getAllCategory() {
        return new ResponseEntity<Response>(
                new Response(true, "Thành công", categoryService.findAll()),
                HttpStatus.OK);
    }

    @Operation(summary = "Lấy thông tin danh mục theo ID")
    @PostMapping(path = "/getCategory")
    public ResponseEntity<?> getCategory(@Validated @RequestParam("id") Long id) {
        Optional<Category> category = categoryService.findById(id);
        if (category.isPresent()) {
            return new ResponseEntity<Response>(
                    new Response(true, "Thành công", category.get()),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<Response>(
                    new Response(false, "Thất bại", null),
                    HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Thêm danh mục mới (có upload icon)")
    @PostMapping(path = "/addCategory", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE })
    public ResponseEntity<?> addCategory(
            @Validated @RequestParam("categoryName") String categoryName,
            @RequestParam(value = "icon", required = false) MultipartFile icon) {

        Optional<Category> optCategory = categoryService.findByCategoryName(categoryName);
        if (optCategory.isPresent()) {
            return new ResponseEntity<Response>(
                    new Response(false, "Category đã tồn tại trong hệ thống", optCategory.get()),
                    HttpStatus.BAD_REQUEST);
        } else {
            Category category = new Category();
            if (icon != null && !icon.isEmpty()) {
                UUID uuid = UUID.randomUUID();
                String uuString = uuid.toString();
                category.setIcon(storageService.getSorageFilename(icon, uuString));
                storageService.store(icon, category.getIcon());
            }
            category.setCategoryName(categoryName);
            categoryService.save(category);
            return new ResponseEntity<Response>(
                    new Response(true, "Thêm Thành công", category),
                    HttpStatus.OK);
        }
    }

    @Operation(summary = "Cập nhật danh mục")
    @RequestMapping(path = "/updateCategory", method = { RequestMethod.PUT, RequestMethod.POST }, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE })
    public ResponseEntity<?> updateCategory(
            @Validated @RequestParam("categoryId") Long categoryId,
            @Validated @RequestParam("categoryName") String categoryName,
            @RequestParam(value = "icon", required = false) MultipartFile icon) {

        Optional<Category> optCategory = categoryService.findById(categoryId);
        if (optCategory.isEmpty()) {
            return new ResponseEntity<Response>(
                    new Response(false, "Không tìm thấy Category", null),
                    HttpStatus.BAD_REQUEST);
        } else {
            Category category = optCategory.get();
            if (icon != null && !icon.isEmpty()) {
                UUID uuid = UUID.randomUUID();
                String uuString = uuid.toString();
                category.setIcon(storageService.getSorageFilename(icon, uuString));
                storageService.store(icon, category.getIcon());
            }
            category.setCategoryName(categoryName);
            categoryService.save(category);
            return new ResponseEntity<Response>(
                    new Response(true, "Cập nhật Thành công", category),
                    HttpStatus.OK);
        }
    }

    @Operation(summary = "Xóa danh mục")
    @DeleteMapping(path = "/deleteCategory")
    public ResponseEntity<?> deleteCategory(@Validated @RequestParam("categoryId") Long categoryId) {
        Optional<Category> optCategory = categoryService.findById(categoryId);
        if (optCategory.isEmpty()) {
            return new ResponseEntity<Response>(
                    new Response(false, "Không tìm thấy Category", null),
                    HttpStatus.BAD_REQUEST);
        } else {
            categoryService.delete(optCategory.get());
            return new ResponseEntity<Response>(
                    new Response(true, "Xóa Thành công", optCategory.get()),
                    HttpStatus.OK);
        }
    }

    @Operation(summary = "Tìm kiếm danh mục có phân trang")
    @GetMapping(path = "/paginated")
    public ResponseEntity<?> getPaginatedCategories(
            @RequestParam(name = "name", defaultValue = "") String name,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size,
            @RequestParam(name = "sort", defaultValue = "categoryId") String sort,
            @RequestParam(name = "dir", defaultValue = "asc") String dir) {

        Sort sortOrder = dir.equalsIgnoreCase("desc") ? Sort.by(sort).descending() : Sort.by(sort).ascending();
        Pageable pageable = PageRequest.of(Math.max(0, page), size, sortOrder);

        Page<Category> result;
        if (StringUtils.hasText(name)) {
            result = categoryService.findByCategoryNameContaining(name, pageable);
        } else {
            result = categoryService.findAll(pageable);
        }

        return new ResponseEntity<Response>(
                new Response(true, "Thành công", result),
                HttpStatus.OK);
    }
}

