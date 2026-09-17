# Project 08: RESTful API & AJAX CRUD với Spring Boot 3 & Swagger 3

Dự án hoàn thiện theo yêu cầu bài thực hành:
1. **CRUD API Category trên Spring Boot 3**: RESTful API với OpenAPI / Swagger 3, quản lý tải lên tập tin và định dạng phản hồi chuẩn (`Response`).
2. **AJAX với RESTful API trong Spring Boot**: Giao diện Bootstrap 5, FontAwesome, jQuery AJAX, xử lý Modal Thêm / Cập nhật / Xóa không cần tải lại toàn bộ trang (Zero-reload).
3. **Mở rộng API & Giao diện AJAX CRUD cho bảng Product và bảng Category**, tích hợp tính năng **tìm kiếm và phân trang**.

---

## 1. Công nghệ sử dụng
- **Java**: 21 / 25
- **Framework**: Spring Boot 3.3.4
  - `spring-boot-starter-web`
  - `spring-boot-starter-thymeleaf` & `thymeleaf-layout-dialect`
  - `spring-boot-starter-data-jpa`
  - `spring-boot-starter-validation`
  - `springdoc-openapi-starter-webmvc-ui` (Swagger 3 cho Spring Boot 3)
  - `commons-io`
  - `lombok`
- **Cơ sở dữ liệu**:
  - H2 Database (Mặc định in-memory, zero-config, tự động khởi tạo dữ liệu mẫu khi chạy)
  - Hỗ trợ cấu hình chuyển sang MySQL / Microsoft SQL Server trong `application.properties`
- **Frontend**:
  - HTML5 / Thymeleaf Layout
  - Bootstrap 5.3.3
  - FontAwesome 6
  - jQuery 3.7.1 AJAX

---

## 2. Cấu trúc dự án
```
project08/
├── pom.xml
├── README.md
├── .gitignore
├── uploads/                        # Thư mục lưu trữ ảnh tải lên
└── src/
    ├── main/
    │   ├── java/
    │   │   └── webprog/
    │   │       ├── SpringbootThymeleafApplication.java
    │   │       ├── configs/
    │   │       │   ├── DataInitializer.java
    │   │       │   ├── OpenAPIConfig.java
    │   │       │   └── StorageProperties.java
    │   │       ├── controllers/
    │   │       │   ├── AdminViewController.java
    │   │       │   ├── ImageUploadController.java
    │   │       │   └── api/
    │   │       │       ├── CategoryAPIController.java
    │   │       │       └── ProductApiController.java
    │   │       ├── entity/
    │   │       │   ├── Category.java
    │   │       │   └── Product.java
    │   │       ├── exception/
    │   │       │   ├── StorageException.java
    │   │       │   └── StorageFileNotFoundException.java
    │   │       ├── model/
    │   │       │   ├── ProductModel.java
    │   │       │   └── Response.java
    │   │       ├── repository/
    │   │       │   ├── CategoryRepository.java
    │   │       │   └── ProductRepository.java
    │   │       └── services/
    │   │           ├── ICategoryService.java
    │   │           ├── IProductService.java
    │   │           ├── IStorageService.java
    │   │           └── impl/
    │   │               ├── CategoryServiceImpl.java
    │   │               ├── FileSystemStorageServiceImpl.java
    │   │               └── ProductServiceImpl.java
    │   └── resources/
    │       ├── application.properties
    │       ├── static/
    │       └── templates/
    │           ├── fragments/
    │           │   ├── footer.html
    │           │   └── header.html
    │           ├── layouts/
    │           │   └── layout.html
    │           ├── categories/
    │           │   └── ajax.html
    │           └── products/
    │               └── ajax.html
    └── test/
        └── java/
            └── webprog/
                └── ApplicationTests.java
```

---

## 3. Hướng dẫn chạy ứng dụng

### Chạy bằng Maven:
```bash
mvn spring-boot:run
```
Hoặc đóng gói JAR:
```bash
mvn clean package -DskipTests
java -jar target/project08-1.0-SNAPSHOT.jar
```

Ứng dụng khởi chạy trên cổng **8082**:
- **Trang chủ / Danh mục (Category AJAX)**: [http://localhost:8082/admin/categories](http://localhost:8082/admin/categories)
- **Quản lý Sản phẩm (Product AJAX)**: [http://localhost:8082/admin/products](http://localhost:8082/admin/products)
- **Swagger 3 (OpenAPI UI)**: [http://localhost:8082/swagger-ui/index.html](http://localhost:8082/swagger-ui/index.html) hoặc [http://localhost:8082/swagger-ui.html](http://localhost:8082/swagger-ui.html)
- **H2 Console**: [http://localhost:8082/h2-console](http://localhost:8082/h2-console) (JDBC URL: `jdbc:h2:mem:webprog_db`, User: `sa`, Password: để trống)

---

## 4. Danh sách RESTful API

### Category API (`/api/category`):
- `GET /api/category`: Lấy danh sách tất cả danh mục.
- `POST /api/category/getCategory?id={id}`: Lấy chi tiết danh mục theo ID.
- `POST /api/category/addCategory`: Thêm mới danh mục (form-data: `categoryName`, `icon`).
- `PUT /api/category/updateCategory`: Cập nhật danh mục (`categoryId`, `categoryName`, `icon`).
- `DELETE /api/category/deleteCategory?categoryId={id}`: Xóa danh mục.
- `GET /api/category/paginated?name={name}&page={page}&size={size}`: Tìm kiếm và phân trang danh mục.

### Product API (`/api/product`):
- `GET /api/product`: Lấy danh sách tất cả sản phẩm.
- `POST /api/product/getProduct?id={id}`: Lấy chi tiết sản phẩm theo ID.
- `POST /api/product/addProduct`: Thêm sản phẩm mới (form-data: `productName`, `unitPrice`, `discount`, `quantity`, `description`, `categoryId`, `status`, `imageFile`).
- `PUT /api/product/updateProduct`: Cập nhật sản phẩm.
- `DELETE /api/product/deleteProduct?productId={id}`: Xóa sản phẩm.
- `GET /api/product/paginated?name={name}&categoryId={categoryId}&page={page}&size={size}`: Tìm kiếm theo tên, lọc theo danh mục và phân trang sản phẩm.

### Image Serving API:
- `GET /admin/categories/images/{filename}`: Phục vụ xem ảnh biểu tượng danh mục.
- `GET /admin/products/images/{filename}`: Phục vụ xem ảnh sản phẩm.

